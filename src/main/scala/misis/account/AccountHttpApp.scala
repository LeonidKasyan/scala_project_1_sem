package misis.account

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object AccountHttpApp extends App{
  implicit val system: ActorSystem = ActorSystem("AccountApp")



  val route: Route =
    path("hello"){
        complete("Hello scala world!")
    }
  Http().newServerAt("0.0.0.0", port = 8080).bind(route)
  StdIn.readLine()
}
