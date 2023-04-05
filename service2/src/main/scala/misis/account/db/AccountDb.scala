
package misis.account.db

import misis.account.model.Account
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.util.UUID

object  AccountDb {
    class AccountTable(tag: Tag) extends Table[Account](tag, "account"){
        val id = column[UUID]("id", O.PrimaryKey)
        val numberPhone = column[Int]("numberPhone")
        val money = column[Int]("money")

        def * = (id, numberPhone, money) <> ((Account.apply _).tupled, Account.unapply)
    }

    val accountTable = TableQuery[AccountTable]
}
