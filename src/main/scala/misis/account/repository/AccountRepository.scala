package misis.account.repository

import misis.account.model._
import java.util.UUID

trait AccountRepository {
    def list(): List[Account]
    def createAccount(account: CreateAccount): Account
    def updateAccountNumberPhone(account: UpdateAccountNumberPhone): Option[Account]
    def UpdateAccountMoney(account: UpdateAccountMoney): Option[Account]
    def deleteAccount(id:UUID): Option[Account]
    }
