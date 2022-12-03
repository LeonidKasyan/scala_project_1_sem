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
  val acc1 = repository.create(CreateAccount(1111, 500))
  // обновление номера телефона
  val acc2 = repository.create(CreateAccount(2222, 400))
  repository.updateNumberPhone(UpdateAccountNumberPhone(acc2.id, 2223))
  // удаление счета
  val acc3 = repository.create(CreateAccount(3333, 300))
  repository.delete(acc3.id)
  // Пополнение счета
  val acc4 = repository.create(CreateAccount(4444, 0))
  repository.updateMoneyPlus(UpdateAccountMoneyPlus(acc4.id, 500))
  // Снять денги со счета
  val acc5 = repository.create(CreateAccount(5555, 500))
  repository.updateMoneyMinus(UpdateAccountMoneyMinus(acc5.id, 400))


  val route: Route =
    (path("hello") & get){
        complete("Hello scala world!")
    } ~
        (path("accounts")& get) {
          val list =  repository.list()
          complete(list)
        } ~
        path("cudaccount"){ //cud - create update delete
          (post & entity(as[CreateAccount])) { newAccount =>
            complete(repository.create(newAccount))
          }
        } ~
        path("cudaccount" / JavaUUID){ id=>
          get{
            complete(repository.get(id))
          }
        } ~
        path("cudaccount"){ 
          (put & entity(as[UpdateAccountNumberPhone])){
            updateNumberPhone =>
            complete(repository.updateNumberPhone(updateNumberPhone))
          }
        } ~
        path("cudaccount" / JavaUUID){ id=>
          delete{
            complete(repository.delete(id))
          }
        }




  Http().newServerAt("0.0.0.0", port = 8080).bind(route)
  StdIn.readLine()
}
