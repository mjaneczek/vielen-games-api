package controllers

import DAOs.UserDAO
import com.mongodb.casbah.commons.MongoDBObject
import models.User
import play.api.libs.json.{JsValue, JsObject}
import play.api.mvc._
import scala.concurrent.Future

case class APIAction[A](currentUser: User, params: Map[String, JsValue], request: Request[A]) extends WrappedRequest[A](request)

object APIAction extends ActionBuilder[APIAction] with play.api.mvc.Results {
  def invokeBlock[A](request: Request[A], block: (APIAction[A]) => Future[Result]) = {
    val anyContentRequest = request.asInstanceOf[Request[AnyContent]]

    currentUser(anyContentRequest).map { user =>
      block(new APIAction(user, params(anyContentRequest), request))
    } getOrElse {
      Future.successful(Forbidden)
    }
  }

  def currentUser(request : Request[AnyContent]) = {
    val authToken = request.headers.get("X-Auth-Token").get
    UserDAO.findOne(MongoDBObject("authenticateTokens" -> authToken))
  }

  def params(request : Request[AnyContent]) = {
    request.body.asJson.map(json => json.as[JsObject].fields.toMap).orNull
  }
}