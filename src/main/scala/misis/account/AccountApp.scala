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
import misis.account.route._

object AccountApp extends App with FailFastCirceSupport {
  implicit val system: ActorSystem = ActorSystem("AccountApp")
  implicit val ec = system.dispatcher
  val repository = new AccountRepositoryMutable
  val helloRoute = new HelloRoute().route
  val accountRoute = new AccountRoute(repository).route


  Http().newServerAt("0.0.0.0", port = 8080).bind(helloRoute ~ accountRoute)
  println("[info] server is running, enter any character to disable it")
  StdIn.readLine()
}
