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
      "teams" -> JsObject(teams.map(team => (team, serializeTeamState(game, team)))) ::
      "walls" -> JsArray(wallPositions(game)) ::
      "active_team" -> JsString(game.activeTeam) :: Nil
    )
  }

  def teams = {
    List("team_1", "team_2")
  }

  def serializeTeamState(game : Game, team : String) = {
    JsObject(
      "pawn_position" -> JsString(playerByTeam(game, team).pawnPosition) ::
      "walls_left" -> JsNumber(BigDecimal(playerByTeam(game, team).wallsLeft)) :: Nil
    )
  }

  def playerByTeam(game : Game, team : String) = {
    game.players.find(player => player.team == team).get
  }

  def wallPositions(game : Game) = {
    gameWalls(game).map(move => JsString(move.position))
  }

  def gameWalls(game : Game) = {
    game.moves.filter(move => move.moveType == "wall")
  }
}
