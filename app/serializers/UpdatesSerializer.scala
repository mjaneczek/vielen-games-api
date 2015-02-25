package serializers

import models.Game
import play.api.libs.json.{JsNumber, JsArray, JsObject, JsString}

class UpdatesSerializer(games: List[Game]) {

  def toJson = {
    JsArray(games.map(game => serializeGame(game)))
  }

  private def serializeGame(game: Game) = {
    JsObject(
      "id" -> JsString(game.id.toString) ::
      "game_type" -> JsString("kuridor") ::
      "winner" -> new PlayerSerializer(game.winner).toJson ::
      "players" -> JsArray(game.players.map(player => new PlayerSerializer(player).toJson)) ::
      "moves" -> JsArray(game.moves.map(move => new MoveSerializer(move).toJson)) ::
      "current_state" -> serializeCurrentState(game) :: Nil
    )
  }

  private def serializeCurrentState(game: Game) = {
    JsObject(
      "teams" -> JsObject(teams.map(team => (team, serializeTeamState(team)))) ::
      "walls" -> JsArray(wallPositions(game)) ::
      "active_team" -> JsString("team_1") :: Nil
    )
  }

  def teams = {
    List("team_1", "team_2")
  }

  def serializeTeamState(team : String) = {
    JsObject(
      "pawn_position" -> JsString(startPosition(team)) ::
      "walls_left" -> JsNumber(10) :: Nil
    )
  }

  def startPosition(team: String) = {
    if(team == "team_1") "e1" else "e9"
  }

  def wallPositions(game : Game) = {
    gameWalls(game).map(move => JsString(move.position))
  }

  def gameWalls(game : Game) = {
    game.moves.filter(move => move.moveType == "wall")
  }
}
