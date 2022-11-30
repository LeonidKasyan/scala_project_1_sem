package misis.account.repository

import misis.account.model._
import java.util.UUID

trait AccountRepository {
  def list(): List[Account]
  def createAccount(account: CreateAccount): Account
  def updateAccountNumberPhone(account: UpdateAccountNumberPhone): Option[Account]
  def updateAccountMoneyPlus(account: UpdateAccountMoneyPlus): Option[Account]
  def updateAccountMoneyMinus(account: UpdateAccountMoneyMinus): Option[Account]
  def deleteAccount(id: UUID): Option[Account]
}
