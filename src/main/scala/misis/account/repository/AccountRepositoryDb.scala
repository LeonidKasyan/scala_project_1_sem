package misis.account.repository

import misis.account.model._
import scala.concurrent.Future
import java.util.UUID
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._


class AccountRepositoryDb(implicit val ec: ExecutionContext, db: Database) extends AccountRepository {

  override def list(): Future[List[Account]] = ???

  override def get(id: UUID): Future[Account] = ???

  override def create(account: CreateAccount): Future[Account] = ???

  override def updateNumberPhone(account: UpdateAccountNumberPhone): Future[Option[Account]] = ???

  override def updateMoneyPlus(account: UpdateAccountMoneyPlus): Future[Option[Account]] = ???

  override def updateMoneyMinus(account: UpdateAccountMoneyMinus): Future[Option[Account]] = ???

  override def delete(id: UUID): Future[Option[Account]] = ???

  
}
