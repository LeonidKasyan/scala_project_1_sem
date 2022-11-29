package misis.account

import misis.account.model._
import misis.account.repository._

object AccountApp extends App {

    val repository = new AccountRepositoryMutable

    //содздание счета 
    val acc1 = repository.createAccount(CreateAccount(1111,500))
    //обновление номера телефона
    val acc2 = repository.createAccount(CreateAccount(2222,400))
    repository.updateAccountNumberPhone(UpdateAccountNumberPhone(acc2.id,2223))
    //удаление счета
    val acc3 = repository.createAccount(CreateAccount(3333,300))
    repository.deleteAccount(acc3.id)
    //Пополнение счета
    val acc4 = repository.createAccount(CreateAccount(4444,0))
    repository.UpdateAccountMoney(UpdateAccountMoney(acc4.id,500))

    println(repository.list())


}
