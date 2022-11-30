package misis.account

import misis.account.model._
import misis.account.repository._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object AccountApp extends App {

  val repository = new AccountRepositoryMutable

  // содздание счета
  val acc1 = repository.createAccount(CreateAccount(1111, 500))
  // обновление номера телефона
  val acc2 = repository.createAccount(CreateAccount(2222, 400))
  repository.updateAccountNumberPhone(UpdateAccountNumberPhone(acc2.id, 2223))
  // удаление счета
  val acc3 = repository.createAccount(CreateAccount(3333, 300))
  repository.deleteAccount(acc3.id)
  // Пополнение счета
  val acc4 = repository.createAccount(CreateAccount(4444, 0))
  repository.updateAccountMoneyPlus(UpdateAccountMoneyPlus(acc4.id, 500))
  // Снять денги со счета
  val acc5 = repository.createAccount(CreateAccount(5555, 500))
  repository.updateAccountMoneyMinus(UpdateAccountMoneyMinus(acc5.id, 400))

  private val list =  repository.list()
  val result = list.asJson.spaces2
  println(result)

}
