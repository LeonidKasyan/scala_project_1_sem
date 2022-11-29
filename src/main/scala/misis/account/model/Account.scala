package misis.account.model


import java.util.UUID

case class Account(id: UUID, numberPhone: Int, money: Int)

case class CreateAccount(numberPhone: Int, money: Int)

case class UpdateAccountNumberPhone(id: UUID, numberPhone: Int)

case class UpdateAccountMoney(id: UUID, money: Int)