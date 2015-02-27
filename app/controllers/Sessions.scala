package controllers

import DAOs.{UserDAO, GameDAO}
import com.mongodb.casbah.commons.MongoDBObject
import com.restfb.DefaultFacebookClient
import interactors.{GetUpdatesInteractor, LoginInteractor}
import org.joda.time.DateTime
import play.api.libs.json.{JsString, JsObject}
import play.api.mvc._
import serializers.{UpdatesSerializer, SessionSerializer}

object Sessions extends Controller {

  def create = Action(parse.tolerantJson) { request =>
    val params = request.body.as[JsObject].fields.toMap // there must be less ugly solution!

    val user = new LoginInteractor(new DefaultFacebookClient(params("provider_token").as[String])).call
    Ok(new SessionSerializer(user).toJson)
  }

  def updates(since : Option[String]) = Action { request =>
    val sinceDate = since.map(since => DateTime.parse(since)).orNull

    Ok(JsObject(
      "until" -> JsString(DateTime.now().toString) ::
      "games" -> new UpdatesSerializer(new GetUpdatesInteractor(currentUser(request), sinceDate).call).toJson :: Nil
    ))
  }

  def currentUser(request : Request[AnyContent]) = {
    val authToken = request.headers.get("X-Auth-Token").get
    UserDAO.findOne(MongoDBObject("authenticateTokens" -> authToken)).get
  }
}