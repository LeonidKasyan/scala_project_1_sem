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

class TransferAccountRoute(repository: AccountRepository)(implicit ec: ExecutionContext) extends  FailFastCirceSupport {
    def route = 
        path("transfer"){ 
        (put & entity(as[TransferAccount])){moneyTransfer =>
        onSuccess(repository.moneyTransfer(moneyTransfer)){
            case Right(value) => complete(value)
            case Left(s) => complete(StatusCodes.NotAcceptable,s)
            }
        
        }
    } ~
          path("transfer" / "external") {
              (put & entity(as[TransferAccount])) { moneyTransfer =>
                  onSuccess(repository.moneyTransfer(moneyTransfer)) {
                      case Right(value) => complete(value)
                      case Left(s) => complete(StatusCodes.NotAcceptable, s)
                  }
              }
          }
}
