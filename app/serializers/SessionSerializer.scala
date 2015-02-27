package serializers

import models.User
import play.api.libs.json.{JsString, JsObject}

class SessionSerializer(user : User) {
  def toJson = {
    JsObject(
      "auth_token" -> JsString(user.authenticateTokens.last) ::
      "user" -> new UserSerializer(user).toJson :: Nil
    )
  }
}

