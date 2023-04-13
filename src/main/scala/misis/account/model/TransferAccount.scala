package misis.account.model

import java.util.UUID
import misis.account.model._

case class TransferAccount (idPlus: UUID,idMinus: UUID, moneyChange: Int)
case class ChangeAccountResult(idMinus: UUID, moneyResult: Int)
case class Checkout(id: UUID, money: Int)