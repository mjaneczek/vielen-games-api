package interactors

import DAOs.GameDAO
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import models.{Move, Game}
import org.joda.time.DateTime

class CreateMoveInteractor(game : Game, move: Move) {
  def call = {
    updateGame()
  }

  private def updateGame() = {
    GameDAO.update(MongoDBObject("_id" -> game.id), updatedGame, upsert = false, multi = false, new WriteConcern)
  }

  private def updatedGame = {
    game.copy(activeTeam = nextTeam, players = updatedPlayers, moves = game.moves ::: List(move), updatedAt = DateTime.now())
  }

  private def updatedPlayers = {
    List(updatedActivePlayer, nonActivePlayer)
  }

  private def updatedActivePlayer = {
    move.moveType match {
      case "pawn" => activePlayer.copy(pawnPosition = move.position)
      case "wall" => activePlayer.copy(wallsLeft = activePlayer.wallsLeft - 1)
    }
  }

  private def activePlayer = {
    game.players.find(player => player.team == game.activeTeam).get
  }

  private def nonActivePlayer = {
    game.players.filterNot(player => player == activePlayer).head
  }

  private def nextTeam = {
    game.activeTeam match {
      case "team_1" => "team_2"
      case "team_2" => "team_1"
    }
  }
}