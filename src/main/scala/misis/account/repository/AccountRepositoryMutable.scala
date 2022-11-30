package misis.account.repository

import misis.account.model._
import misis.account.repository._

import java.util.UUID
import scala.collection.mutable

class AccountRepositoryMutable extends AccountRepository {
  private val store = mutable.Map[UUID, Account]()

  override def list(): scala.List[Account] = {
    store.values.toList
  }
  override def createAccount(create: CreateAccount): Account = {
    val account = Account(
      id = UUID.randomUUID(),
      numberPhone = create.numberPhone,
      money = create.money
    )
    store.put(account.id, account)
    account
  }
  override def updateAccountNumberPhone(update: UpdateAccountNumberPhone): Option[Account] = {
    store.get(update.id).map { account =>
      val updated = account.copy(numberPhone = update.numberPhone)
      store.put(account.id, updated)
      updated
    }
  }

  override def updateAccountMoneyPlus(update: UpdateAccountMoneyPlus): Option[Account] = {
    store.get(update.id).map { account =>
    //   if(update.money < 0) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money + update.money)
      store.put(account.id, updated)
      updated
    }
  }
    override def updateAccountMoneyMinus(update: UpdateAccountMoneyMinus): Option[Account] = {
    store.get(update.id).map { account =>
    //   if(update.money < 0 | account.money < update.money) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money - update.money)
      store.put(account.id, updated)
      updated
    }
  }
  override def deleteAccount(id: UUID): Option[Account] = {
    store.remove(id)
  }
}
