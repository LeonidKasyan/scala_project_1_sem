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
  override def get(id: UUID): Account = {
    store(id)
  }
  override def create(createAccount: CreateAccount): Account = {
    val account = Account(
      id = UUID.randomUUID(),
      numberPhone = createAccount.numberPhone,
      money = createAccount.money
    )
    store.put(account.id, account)
    account
  }
  override def updateNumberPhone(update: UpdateAccountNumberPhone): Option[Account] = {
    store.get(update.id).map { account =>
      val updated = account.copy(numberPhone = update.numberPhone)
      store.put(account.id, updated)
      updated
    }
  }

  override def updateMoneyPlus(update: UpdateAccountMoneyPlus): Option[Account] = {
    store.get(update.id).map { account =>
    //   if(update.money < 0) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money + update.money)
      store.put(account.id, updated)
      updated
    }
  }
    override def updateMoneyMinus(update: UpdateAccountMoneyMinus): Option[Account] = {
    store.get(update.id).map { account =>
    //   if(update.money < 0 | account.money < update.money) {
    //     update.money = 0
    //   }
      val updated = account.copy(money = account.money - update.money)
      store.put(account.id, updated)
      updated
    }
  }
  override def delete(id: UUID): Option[Account] = {
    store.remove(id)
  }

}
