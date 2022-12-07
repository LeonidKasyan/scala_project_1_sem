package misis.account.repository

import misis.account.model._
import misis.account.repository._

import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class AccountRepositoryInMemory(implicit val ec: ExecutionContext) extends AccountRepository {
  private val myCart = mutable.Map[UUID, Account]()

  override def list():  Future[scala.List[Account]] = Future{
    myCart.values.toList
  }
  override def get(id: UUID): Future[Account] = Future{
    myCart(id)
  }
  override def create(createAccount: CreateAccount): Future[Account] = Future{
    val account = Account(
      id = UUID.randomUUID(),
      numberPhone = createAccount.numberPhone,
      money = createAccount.money
    )
    myCart.put(account.id, account)
    account
  }
  override def updateNumberPhone(update: UpdateAccountNumberPhone): Future[Option[Account]] = Future{
    myCart.get(update.id).map { account =>
      val updated = account.copy(numberPhone = update.numberPhone)
      myCart.put(account.id, updated)
      updated
    }
  }

  override def updateMoneyPlus(update: UpdateAccountMoneyPlus): Future[Option[Account]] = Future{
    myCart.get(update.id).map { account =>
    //   if(update.money < 0) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money + update.money)
      myCart.put(account.id, updated)
      updated
    }
  }
    override def updateMoneyMinus(update: UpdateAccountMoneyMinus): Future[Option[Account]] = Future{
    myCart.get(update.id).map { account =>
    //   if(update.money < 0 | account.money < update.money) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money - update.money)
      myCart.put(account.id, updated)
      updated
    }
  }
  override def delete(id: UUID): Future[Unit] = Future{
    myCart.remove(id)
  }

}
