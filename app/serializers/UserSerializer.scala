package serializers

import models.{User}
import play.api.libs.json.{JsObject, JsString}

class UserSerializer(user: User) {

  def toJson = {
    JsObject(
      "id" -> JsString(user.id.toString) ::
      "name" -> JsString(user.name) ::
      "avatar_url" -> JsString(s"http://graph.facebook.com/${user.providerId}/picture?width=200&height=200") :: Nil
    )
  }
}
