package misis.account.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.util.{Success,Failure}
import scala.concurrent.ExecutionContext

import misis.account.model._
import misis.account.repository._
import akka.actor.Status


class AccountRoute(repository: AccountRepository)(implicit ec: ExecutionContext) extends  FailFastCirceSupport {
  def route = 
    (path("accounts")& get) {
        val list =  repository.list()
        complete(list)
    } ~
    path("account"){
        (post & entity(as[CreateAccount])) { newAccount =>
        complete(repository.create(newAccount))
        }
    } ~
    path("account" / JavaUUID){ id=>
        get{
        complete(repository.get(id))
        }
    } ~
    path("account"){ 
        (put & entity(as[UpdateAccountNumberPhone])){
        updateNumberPhone =>
        complete(repository.updateNumberPhone(updateNumberPhone))
        }
    } ~
    path("account" / JavaUUID){ id=>
        delete{
        complete(repository.delete(id))
        }
        
    } ~
    path("account" /"money" / "plus"){ 
        (put & entity(as[UpdateAccountMoneyPlus])){updateMoneyPlus =>
        onSuccess(repository.updateMoneyPlus(updateMoneyPlus)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable,s)
            }
        }
    } ~
    path("account" /"money" / "minus"){ 
        (put & entity(as[UpdateAccountMoneyMinus])){updateMoneyMinus =>
        onSuccess(repository.updateMoneyMinus(updateMoneyMinus)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable,s)
            }
        
        }
    }~
    path("transfer"){ 
        (put & entity(as[TransferAccount])){moneyTransfer =>
        onSuccess(repository.moneyTransfer(moneyTransfer)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable,s)
            }
        
        }
    }
}
