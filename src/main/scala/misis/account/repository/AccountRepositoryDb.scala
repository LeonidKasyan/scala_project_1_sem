package misis.account.repository

import misis.account.model._
import scala.concurrent.Future
import java.util.UUID
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import misis.account.db.AccountDb._


class AccountRepositoryDb(implicit val ec: ExecutionContext, db: Database) extends AccountRepository {

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

  override def updateMoneyPlus(account: UpdateAccountMoneyPlus): Future[Option[Account]] = {
    //  moneyBack <- accountTable.filter(_.id === account.id).map(_.money).result.head

     for {
            moneyBack <- find(account.id)
            _ <- db.run {
                accountTable
                    .filter(_.id === account.id)
                    .map(_.money)
                    .update(moneyBack.get.money + account.money)
            }
            res <- find(account.id)
            
        } yield res
  }

  override def updateMoneyMinus(account: UpdateAccountMoneyMinus): Future[Option[Account]] = {
     for {
            moneyBack <- find(account.id)
            _ <- db.run {
                accountTable
                    .filter(_.id === account.id)
                    .map(_.money)
                    .update(moneyBack.get.money - account.money)
            }
            res <- find(account.id)
        } yield res
  }

  override def delete(id: UUID): Future[Unit] ={
     db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }

  
}
