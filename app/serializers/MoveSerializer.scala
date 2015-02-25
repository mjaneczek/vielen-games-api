package serializers

import models.{Move}
import play.api.libs.json.{JsValue, JsObject, JsString}

class MoveSerializer(move: Move) {

  def toJson : JsValue = {
    JsObject(
      "position" -> JsString(move.position) ::
      "move_type" -> JsString(move.moveType) :: Nil
    )
  }
}
