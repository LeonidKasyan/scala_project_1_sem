package misis.account.model

import java.util.UUID

case class Account(id: UUID, numberPhone: Int, money: Int)

case class CreateAccount(numberPhone: Int, money: Int)

case class UpdateAccountNumberPhone(id: UUID, numberPhone: Int)

case class UpdateAccountMoneyPlus(id: UUID, money: Int)
case class UpdateAccountMoneyMinus(id: UUID, money: Int)
