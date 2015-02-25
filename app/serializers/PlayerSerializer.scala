package serializers

import models.Player
import play.api.libs.json.{JsValue, JsNull, JsObject, JsString}

class PlayerSerializer(player: Player) {

  def toJson : JsValue = {
    if(player == null) {
      return JsNull
    }

    JsObject(
      "id" -> JsString(player.id.toString) ::
      "name" -> JsString(player.name) ::
      "avatar_url" -> JsString(s"http://graph.facebook.com/${player.providerId}/picture?width=200&height=200") ::
      "team" -> JsString(player.team) :: Nil
    )
  }
}
