package interactors

import DAOs.GameDAO
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import models.{Player, Move, Game}
import org.joda.time.DateTime

class CreateMoveInteractor(game : Game, player: Player, move: Move) {
  def call = {
    updateGame()
  }

  def updateGame() = {
    GameDAO.update(MongoDBObject("_id" -> game.id), updatedGame, upsert = false, multi = false, new WriteConcern)
  }

  private def updatedGame = {
    game.copy(activeTeam = nextTeam, moves = game.moves ::: List(move), updatedAt = DateTime.now())
  }

  def nextTeam = {
    game.activeTeam match {
      case "team_1" => "team_2"
      case "team_2" => "team_1"
    }
  }
}