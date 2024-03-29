package misis.account.repository

import misis.account.model._
import scala.concurrent.Future
import java.util.UUID
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import misis.account.db.AccountDb._
import scala.Error
import scala.util.Either
import cats.instances.future
import org.checkerframework.checker.units.qual.s
import misis.account.repository.PaymentClient


class AccountRepositoryDb(paymentClient: PaymentClient)(implicit val ec: ExecutionContext, db: Database) extends AccountRepository {

  override def list(): Future[Seq[Account]] = {
     db.run(accountTable.result)
  }

  override def get(id: UUID): Future[Account] = {
    db.run(accountTable.filter(i => i.id === id).result.head)
  }

  def find(id: UUID): Future[Option[Account]] = {
        db.run(accountTable.filter(i => i.id === id).result.headOption)
    }
  override def create(createAccount: CreateAccount): Future[Account] = {
    val account = Account(numberPhone = createAccount.numberPhone, money = createAccount.money)
        for {
            _ <- db.run(accountTable += account)
            res <- get(account.id)
        } yield res
  }

  override def updateNumberPhone(account: UpdateAccountNumberPhone): Future[Option[Account]] = {
    for {
            _ <- db.run {
                accountTable
                    .filter(_.id === account.id)
                    .map(_.numberPhone)
                    .update(account.numberPhone)
            }
            res <- find(account.id)
        } yield res
  }

  override def updateMoneyPlus(account: UpdateAccountMoneyPlus): Future[Either[String,Account]] = {
   val query = accountTable
                .filter(_.id === account.id)
                .map(_.money)
    for {
        oldBalansOpt <- db.run(query.result.headOption)
        deltaBalans = account.money
        updateBalans = oldBalansOpt.map{oldBalans =>
          Right( oldBalans + deltaBalans)
          }.getOrElse(Left("Не найдено элемент"))
        future = updateBalans.map(balans => db.run {query.update(balans)}) match {
          case Right(futute) => futute.map(Right(_))
          case Left(s) => Future.successful(Left(s))
          
        }
        updated <- future
        res <- find(account.id)   
        res2 = updated.map(_ => res.get) 
        } yield res2
  }

  override def updateMoneyMinus(account: UpdateAccountMoneyMinus): Future[Either[String,Account]] = {
    val query = accountTable
                .filter(_.id === account.id)
                .map(_.money)
    for {
        oldBalansOpt <- db.run(query.result.headOption)
        deltaBalans = account.money
        updateBalans = oldBalansOpt.map{oldBalans =>
            if ((oldBalans - deltaBalans) < 0)
              Left("Недостаточно денег на счету")
            else Right( oldBalans - deltaBalans)
          }.getOrElse(Left("Не найдено элемент"))
        future = updateBalans.map(balans => db.run {query.update(balans)}) match {
          case Right(futute) => futute.map(Right(_))
          case Left(s) => Future.successful(Left(s))
          
        }
        updated <- future
        res <- find(account.id)   
        res2 = updated.map(_ => res.get) 
        } yield res2
  }


  override def moneyTransfer(transferAccounts: TransferAccount): Future[Either[String, ChangeAccountResult]] = {
    val accountMinus =
      accountTable.filter(_.id === transferAccounts.idMinus).map(_.money)
    val accountPlus =
      accountTable.filter(_.id === transferAccounts.idPlus).map(_.money)
    for {

      accountMinusOpt <- db.run(accountMinus.result.headOption) //  аккаунт отправителя
      accountPlusOpt <- db.run(accountPlus.result.headOption) // аккаунт получателя
      transferMoney = transferAccounts.moneyChange // сумма денег с которой мы работаем

      accountMinusUpd = accountMinusOpt.map { sendlerMoney => {
        if (sendlerMoney >= transferMoney)
          Right(sendlerMoney - transferMoney)
        else
          Left("Недостаточно денег для перевода")
      }
      }.getOrElse(Left("Счет не найден"))
      accountPlusUpd = accountMinusOpt.map { recipientMoney => {
        Right(recipientMoney + transferMoney)
      }
      }.getOrElse(Left("Счет не найден"))

      accountMinusFuture = accountMinusUpd.map { money =>
        db.run {
          accountMinus.update(money)
        }
      } match {
        case Right(future) =>
          paymentClient.payment(UpdateAccountMoneyPlus(transferAccounts.idPlus, transferAccounts.moneyChange))
            .flatMap {
              case Right(_) =>
                Future.successful(Right("Деньги успешно переведены"))
              case Left(_) =>
                db.run(accountMinus.update(accountMinusOpt.get + transferMoney)).map {
                  case 0 => Left("Не удалось обновить баланс счета отправителя")
                  case _ => Right("Деньги успешно возвращены")
                }
            }


        case Left(s) => Future.successful(Left(s))
      }
      updated <- accountMinusFuture
      res <- find(transferAccounts.idMinus)
    } yield updated.map(_ =>
      ChangeAccountResult(transferAccounts.idMinus, res.get.money))

  }





  override def delete(id: UUID): Future[Unit] ={
     db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }


}

