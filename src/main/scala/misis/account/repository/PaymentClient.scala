package misis.account.repository

import java.util.UUID
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, MediaTypes, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import misis.account.model
import misis.account.model.{Account, UpdateAccountMoneyPlus}


import scala.concurrent.{ExecutionContext, Future}


class PaymentClient (implicit val ec: ExecutionContext, actorSystem: ActorSystem) extends FailFastCirceSupport{
  def payment(updateAccountMoneyPlus: UpdateAccountMoneyPlus): Future[Either[String,Account]]  = {
    val request = HttpRequest(
      method = HttpMethods.PUT,
      uri = s"http://localhost:8081/account/money/plus",
      entity = HttpEntity(MediaTypes.`application/json`,updateAccountMoneyPlus.asJson.noSpaces)
    )
    val future = for {
      response <- Http().singleRequest(request)
      result <- Unmarshal(response).to[Account]
        .map(res => Right(res))
        .recoverWith {
          case _ => Unmarshal(response).to[String].map(Left(_))
        }
    } yield result

    future
  }

}