package misis.account

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory
import scala.io.StdIn
import io.circe.syntax._
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce._
import slick.jdbc.PostgresProfile.api._

import misis.account.db.InitDb
import misis.account.model._
import misis.account.repository._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import misis.account.route._
import misis.account.repository.PaymentClient

object AccountDbApp extends App with FailFastCirceSupport {
  implicit val system: ActorSystem = ActorSystem("AccountApp")
  implicit val ec = system.dispatcher
  implicit val db = Database.forConfig("database.postgres")
  val port = ConfigFactory.load().getInt("port")

  new InitDb().prepare()
  val paymentClient = new PaymentClient
  val repository = new AccountRepositoryDb(paymentClient)
  val helloRoute = new HelloRoute().route
  val accountRoute = new AccountRoute(repository).route
  val transferAccountRoute = new TransferAccountRoute(repository).route


  Http().newServerAt("0.0.0.0", port = port).bind(helloRoute ~ accountRoute ~ transferAccountRoute)
  println("Server is running, enter any character to disable it")
  StdIn.readLine()
}
