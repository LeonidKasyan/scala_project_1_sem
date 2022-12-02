package misis.account

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn
import io.circe.syntax._
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce._

import misis.account.model._
import misis.account.repository._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object AccountHttpApp extends App with FailFastCirceSupport {
  implicit val system: ActorSystem = ActorSystem("AccountApp")
  val repository = new AccountRepositoryMutable

  // содздание счета
  val acc1 = repository.createAccount(CreateAccount(1111, 500))
  // обновление номера телефона
  val acc2 = repository.createAccount(CreateAccount(2222, 400))
  repository.updateAccountNumberPhone(UpdateAccountNumberPhone(acc2.id, 2223))
  // удаление счета
  val acc3 = repository.createAccount(CreateAccount(3333, 300))
  repository.deleteAccount(acc3.id)
  // Пополнение счета
  val acc4 = repository.createAccount(CreateAccount(4444, 0))
  repository.updateAccountMoneyPlus(UpdateAccountMoneyPlus(acc4.id, 500))
  // Снять денги со счета
  val acc5 = repository.createAccount(CreateAccount(5555, 500))
  repository.updateAccountMoneyMinus(UpdateAccountMoneyMinus(acc5.id, 400))


  val route: Route =
    (path("hello") & get){
        complete("Hello scala world!")
    } ~
        (path("accounts")& get) {
          val list =  repository.list()
          complete(list)
        } ~
        path("account"){
          (post & entity(as[CreateAccount])) { newAccount =>
            complete(repository.createAccount(newAccount))
          }
        }




  Http().newServerAt("0.0.0.0", port = 8080).bind(route)
  StdIn.readLine()
}