package interactors

import DAOs.GameDAO
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import models.{User, Move, Game}
import org.joda.time.DateTime
import validators.{WallMoveValidator, PawnMoveValidator}

class CreateMoveInteractor(game : Game, user : User, move: Move) {
  def call = {
    if (canMove) { updateGame() }
    canMove
  }

  private def updateGame() = {
    GameDAO.update(MongoDBObject("_id" -> game.id), updatedGame, upsert = false, multi = false, new WriteConcern)
  }

  private def updatedGame = {
    game.copy(winner = winner, activeTeam = nextTeam, players = updatedPlayers, moves = game.moves ::: List(move), updatedAt = DateTime.now())
  }

  private def updatedPlayers = {
    List(updatedActivePlayer, game.opponent)
  }

  private def updatedActivePlayer = {
    move.moveType match {
      case "pawn" => game.activePlayer.copy(pawnPosition = move.position)
      case "wall" => game.activePlayer.copy(wallsLeft = game.activePlayer.wallsLeft - 1)
    }
  }

  private def winner = {
    if(winningYPosition(game.activeTeam) == move.position(1) && move.moveType == "pawn")
      game.activePlayer
    else
      null
  }

  private val winningYPosition = {
    Map("team_1" -> '9', "team_2" -> '1')
  }

  private def nextTeam : String = {
    if(winner != null)
      return null

    game.activeTeam match {
      case "team_1" => "team_2"
      case "team_2" => "team_1"
    }
  }

  private def canMove : Boolean  = {
    if(game.activePlayer.id != user.id)
      return false

    if(move.moveType == "pawn") {
      new PawnMoveValidator(game, move).isValidMove
    } else {
      new WallMoveValidator(game, move).isValidMove
    }
  }
}