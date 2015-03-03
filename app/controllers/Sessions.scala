package controllers

import com.restfb.DefaultFacebookClient
import interactors.{GetUpdatesInteractor, LoginInteractor}
import org.joda.time.DateTime
import play.api.libs.json.{JsString, JsObject}
import play.api.mvc._
import serializers.{UpdatesSerializer, SessionSerializer}

object Sessions extends Controller {

  def create = Action { request =>
    val user = new LoginInteractor(new DefaultFacebookClient(APIAction.params(request)("provider_token").as[String])).call
    Ok(new SessionSerializer(user).toJson)
  }

  def updates(since : Option[String]) = APIAction { request =>
    val sinceDate = since.map(since => DateTime.parse(since)).orNull

    Ok(JsObject(
      "until" -> JsString(DateTime.now().toString) ::
      "games" -> new UpdatesSerializer(new GetUpdatesInteractor(request.currentUser, sinceDate).call).toJson :: Nil
    ))
  }
}