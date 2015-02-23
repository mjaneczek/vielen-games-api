package controllers

import com.restfb.DefaultFacebookClient
import interactors.LoginInteractor
import org.joda.time.DateTime
import play.api.libs.json.{JsArray, JsString, JsObject}
import play.api.mvc._
import serializers.{SessionSerializer}

object Sessions extends Controller {

  def create = Action(parse.tolerantJson) { request =>
    val params = request.body.as[JsObject].fields.toMap // there must be less ugly solution!

    val user = new LoginInteractor(new DefaultFacebookClient(params("provider_token").as[String])).call
    Ok(new SessionSerializer(user).toJson)
  }

  def updates = Action {
    Ok(JsObject(
      "until" -> JsString(DateTime.now().toString) ::
      "games" -> JsArray() :: Nil
    ))
  }
}