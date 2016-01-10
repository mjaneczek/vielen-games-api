package validators

import models.{Game, Move}
import utils.PossiblePositions
import scala.collection.mutable.ArrayBuffer

class PawnMoveValidator(game : Game, move : Move) {
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

  private def activePlayer = {
    game.players.find(player => player.team == game.activeTeam).get
  }

  private def opponent = {
    game.players.filterNot(player => player == activePlayer).head
  }

  lazy val wallPositions = {
    game.moves.filter((m) => m.moveType == "wall").map((m) => m.position)
  }
}
