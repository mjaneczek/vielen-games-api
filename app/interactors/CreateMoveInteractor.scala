package interactors

import DAOs.GameDAO
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import models.{User, Player, Move, Game}
import org.joda.time.DateTime
import utils.PossiblePositions

import scala.collection.mutable.ArrayBuffer

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

  private def winner = {
    if(winningYPosition(game.activeTeam) == move.position(1) && move.moveType == "pawn")
      activePlayer
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
    if(activePlayer.id != user.id)
      return false

    if(move.moveType == "pawn") {
      new PawnMoveValidator().isValidMove
    } else {
      new WallMoveValidator().isValidMove
    }
  }

  class PawnMoveValidator {
    def isValidMove = {
      val validMoves = ArrayBuffer.empty[String]

      val moveLeft  = Map("x" -> -1, "y" -> 0)
      val moveRight = Map("x" -> 1, "y" -> 0)
      val moveUp    = Map("x" -> 0, "y" -> 1)
      val moveDown  = Map("x" -> 0, "y" -> -1)

      for(move <- Array(moveLeft, moveRight, moveUp, moveDown)) {
        val newPosition =  (activePlayer.pawnPosition.charAt(0) + move.get("x").get).toChar.toString + (activePlayer.pawnPosition.charAt(1) +  move.get("y").get).toChar.toString

        if (newPosition == nonActivePlayer.pawnPosition) {
          val jumpNewPosition = (newPosition.charAt(0) + move.get("x").get).toChar.toString + (newPosition.charAt(1) +  move.get("y").get).toChar.toString

          if(isOutOfBoardMove(jumpNewPosition)) {
            if(move.get("x").get == 0) {
              validMoves += ((activePlayer.pawnPosition.charAt(0) - 1).toChar.toString + newPosition.charAt(1).toString)
              validMoves += ((activePlayer.pawnPosition.charAt(0) + 1).toChar.toString + newPosition.charAt(1).toString)
            } else {
              validMoves += (newPosition.charAt(0).toString + (activePlayer.pawnPosition.charAt(1) - 1).toChar.toString)
              validMoves += (newPosition.charAt(0).toString + (activePlayer.pawnPosition.charAt(1) + 1).toChar.toString)
            }
          } else {
            validMoves += jumpNewPosition
          }

        } else {
          if(!isOutOfBoardMove(newPosition)) {
            validMoves += newPosition
          }
        }
      }

      validMoves contains move.position
    }

    private def isOutOfBoardMove(position : String) = {
      !PossiblePositions.pawn.contains(position)
    }
  }

  class WallMoveValidator {
    def isValidMove = {
      activePlayer.wallsLeft > 0 && move.position.matches("[a-h][1-8][vh]") && !wallPositions.contains(move.position) &&
      !wallPositions.map((p) => p.take(2).toString).contains(move.position.take(2).toString) && !isPartiallyOverlapping
    }

    def isPartiallyOverlapping = {
      if(move.position.charAt(2) == 'h') {
        wallPositions.contains((move.position.charAt(0) - 1).toChar.toString + move.position.charAt(1).toString + "h") ||
          wallPositions.contains((move.position.charAt(0) + 1).toChar.toString + move.position.charAt(1).toString + "h")
      } else {
        wallPositions.contains(move.position.charAt(0).toString + (move.position.charAt(1) - 1).toChar.toString + "v") ||
          wallPositions.contains(move.position.charAt(0).toString + (move.position.charAt(1) + 1).toChar.toString + "v")
      }
    }

    lazy val wallPositions = {
      game.moves.filter((m) => m.moveType == "wall").map((m) => m.position)
    }
  }
}