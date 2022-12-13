package misis.account.repository

import misis.account.model._
import java.util.UUID
import scala.concurrent.Future
import scala.util.Either

trait AccountRepository {
  def list(): Future[Seq[Account]]
  def get(id: UUID):  Future[Account]
  def create(account: CreateAccount):  Future[Account]
  def updateNumberPhone(account: UpdateAccountNumberPhone):  Future[Option[Account]]
  def updateMoneyPlus(account: UpdateAccountMoneyPlus):  Future[Either[String,Account]]
  def updateMoneyMinus(account: UpdateAccountMoneyMinus):  Future[Either[String,Account]]
  def moneyTransfer(transferAccounts:TransferAccount):  Future[Either[String,ChangeAccountResult]]
  def delete(id: UUID):  Future[Unit]
}
