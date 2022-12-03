package misis.account.repository

import misis.account.model._
import java.util.UUID

trait AccountRepository {
  def list(): List[Account]
  def get(id: UUID): Account
  def create(account: CreateAccount): Account
  def updateNumberPhone(account: UpdateAccountNumberPhone): Option[Account]
  def updateMoneyPlus(account: UpdateAccountMoneyPlus): Option[Account]
  def updateMoneyMinus(account: UpdateAccountMoneyMinus): Option[Account]
  def delete(id: UUID): Option[Account]
}
