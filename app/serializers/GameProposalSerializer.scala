package serializers

import models.{User, GameProposal}
import org.joda.time.{DateTime, Seconds}
import play.api.libs.json.{JsArray, JsString, JsObject}

class GameProposalSerializer(game_proposals: List[GameProposal]) {

  def toJson() = {
    JsArray(game_proposals.map(gameProposal => serializeGameProposal(gameProposal)))
  }

  private def serializeGameProposal(gameProposal: GameProposal) = {
    JsObject(
      "id" -> JsString(gameProposal.id.toString) ::
      "game_type" -> JsString("kuridor") ::
      "awaiting_players" -> JsArray(gameProposal.users.map(user => serializeAwaitingPlayer(user))) ::
      "age_in_seconds" -> JsString(ageInSeconds(gameProposal).toString) :: Nil
    )
  }

  private def serializeAwaitingPlayer(user: User) = {
    JsObject(
      "id" -> JsString(user.id.toString) ::
      "avatar_url" -> JsString(user.avatar_url.getOrElse("")) ::
      "name" -> JsString(user.name) :: Nil
    )
  }

  private def ageInSeconds(gameProposal: GameProposal) = {
    Seconds.secondsBetween(gameProposal.created_at, DateTime.now).getSeconds
  }
}
