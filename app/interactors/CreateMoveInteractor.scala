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
    List(updatedActivePlayer, opponent)
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

  private def opponent = {
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

  lazy val wallPositions = {
    game.moves.filter((m) => m.moveType == "wall").map((m) => m.position)
  }

  class PawnMoveValidator {
    val moveLeft  = Map("direction" -> "left",  "x" -> -1, "y" ->  0)
    val moveRight = Map("direction" -> "right", "x" ->  1, "y" ->  0)
    val moveUp    = Map("direction" -> "up",    "x" ->  0, "y" ->  1)
    val moveDown  = Map("direction" -> "down",  "x" ->  0, "y" -> -1)

    val validPositions = ArrayBuffer.empty[String]

    def isValidMove = {
      for(move <- Array(moveLeft, moveRight, moveUp, moveDown)) {
        calculatePossiblePositions(move)
      }

      validPositions contains move.position
    }

    private def calculatePossiblePositions(move : Map[String, Any]) : Unit = {
      if(isBlockedMove(activePlayer.pawnPosition, move))
        return

      if (calculateNewPosition(activePlayer.pawnPosition, move) == opponent.pawnPosition) {
        if(!isBlockedMove(opponent.pawnPosition, move)) {
          addPositionIfValid(opponent.pawnPosition, move)
        } else {
          addPositionsForSideJumping(move)
        }

      } else {
        addPositionIfValid(activePlayer.pawnPosition, move)
      }
    }

    private def addPositionIfValid(position : String, move :  Map[String, Any]) = {
      if(!isBlockedMove(position, move))
        validPositions += calculateNewPosition(position, move)
    }

    private def isBlockedMove(position: String, move : Map[String, Any]) : Boolean = {
      isBlockedByWall(position, move.get("direction").get.toString) || isOutOfBoardMove(calculateNewPosition(position, move))
    }

    private def calculateNewPosition(position: String, move : Map[String, Any]) = {
      (position.charAt(0) + move.get("x").get.asInstanceOf[Int]).toChar.toString + (position.charAt(1) +  move.get("y").get.asInstanceOf[Int]).toChar.toString + move.getOrElse("orientation", "")
    }

    private def addPositionsForSideJumping(move : Map[String, Any]) = {
      if(move.get("x").get == 0) { // up - down
        addPositionIfValid(opponent.pawnPosition, moveLeft)
        addPositionIfValid(opponent.pawnPosition, moveRight)
      } else { // left - right
        addPositionIfValid(opponent.pawnPosition, moveUp)
        addPositionIfValid(opponent.pawnPosition, moveDown)
      }
    }

    private def isBlockedByWall(position: String, direction: String) : Boolean = {
      val blockingWallPositions = Map[String, Array[Map[String, Any]]] (
        "up" ->    Array(Map("x" -> -1, "y" ->  0, "orientation" -> "h"), Map("x" ->  0, "y" ->  0, "orientation" -> "h")),
        "down" ->  Array(Map("x" -> -1, "y" -> -1, "orientation" -> "h"), Map("x" ->  0, "y" -> -1, "orientation" -> "h")),
        "left" ->  Array(Map("x" -> -1, "y" -> -1, "orientation" -> "v"), Map("x" -> -1, "y" ->  0, "orientation" -> "v")),
        "right" -> Array(Map("x" ->  0, "y" -> -1, "orientation" -> "v"), Map("x" ->  0, "y" ->  0, "orientation" -> "v"))
      )

      blockingWallPositions.get(direction).get.exists((move) => wallPositions.contains(calculateNewPosition(position, move)))
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
  }
}