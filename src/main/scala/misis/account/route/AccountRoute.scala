package misis.account.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import misis.account.model._
import misis.account.repository._


class AccountRoute(repository: AccountRepository) extends  FailFastCirceSupport {
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
    path("accountmoneyplus"){ 
        (put & entity(as[UpdateAccountMoneyPlus])){
        updateMoneyPlus =>
        complete(repository.updateMoneyPlus(updateMoneyPlus))
        }
    } ~
    path("accountmoneyminus"){ 
        (put & entity(as[UpdateAccountMoneyMinus])){
        updateMoneyMinus =>
        complete(repository.updateMoneyMinus(updateMoneyMinus))
        }
    }
}
