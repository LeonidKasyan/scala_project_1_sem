package misis.account.repository

import misis.account.model._
import java.util.UUID
import scala.concurrent.Future

trait AccountRepository {
  def list(): Future[List[Account]]
  def get(id: UUID):  Future[Account]
  def create(account: CreateAccount):  Future[Account]
  def updateNumberPhone(account: UpdateAccountNumberPhone):  Future[Option[Account]]
  def updateMoneyPlus(account: UpdateAccountMoneyPlus):  Future[Option[Account]]
  def updateMoneyMinus(account: UpdateAccountMoneyMinus):  Future[Option[Account]]
  def delete(id: UUID):  Future[Option[Account]]
}
