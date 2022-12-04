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
        }



  Http().newServerAt("0.0.0.0", port = 8080).bind(route)
  println("[info] server is running, enter any character to disable it")
  StdIn.readLine()
}
