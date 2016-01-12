package validators

import models.{ShiftDirection, Move, Game}
import utils.PossiblePositions

class WallMoveValidator(game : Game, move : Move) {
  val leftShift  =  ShiftDirection(name = "left",  x = -1, y =  0)
  val rightShift =  ShiftDirection(name = "right", x =  1, y =  0)
  val upShift    =  ShiftDirection(name = "up",    x =  0, y =  1)
  val downShift  =  ShiftDirection(name = "down",  x =  0, y = -1)

  def isValidMove = {
    game.activePlayer.wallsLeft > 0 && move.position.matches("[a-h][1-8][vh]") && !game.wallPositions.contains(move.position) &&
      !game.wallPositions.map((p) => p.take(2).toString).contains(move.position.take(2).toString) && !isPartiallyOverlapping  && !isCompletelyBlockingPawn
  }

  def isPartiallyOverlapping = {
    if(move.position.charAt(2) == 'h') {
      game.wallPositions.contains((move.position.charAt(0) - 1).toChar.toString + move.position.charAt(1).toString + "h") ||
        game.wallPositions.contains((move.position.charAt(0) + 1).toChar.toString + move.position.charAt(1).toString + "h")
    } else {
      game.wallPositions.contains(move.position.charAt(0).toString + (move.position.charAt(1) - 1).toChar.toString + "v") ||
        game.wallPositions.contains(move.position.charAt(0).toString + (move.position.charAt(1) + 1).toChar.toString + "v")
    }
  }

  def isCompletelyBlockingPawn : Boolean = {
    !hasAccessToLine(game.players.find(p => p.team == "team_1").get.pawnPosition, '9') ||
      !hasAccessToLine(game.players.find(p => p.team == "team_2").get.pawnPosition, '1')
  }

  private def hasAccessToLine(pawnPosition : String, line : Char) : Boolean = {
    var allAccessiblePositions = Array[String]()
    var newAccessiblePositions = Array(pawnPosition)

    while (newAccessiblePositions.nonEmpty) {
      for (newPosition <- newAccessiblePositions) {
        if (newPosition.charAt(1) == line) {
          return true
        }
      }

      var newerAccessiblePositions = getAllNeighbours(newAccessiblePositions).toArray
      newerAccessiblePositions = newerAccessiblePositions.diff(allAccessiblePositions)
      allAccessiblePositions = allAccessiblePositions union newAccessiblePositions
      newAccessiblePositions = newerAccessiblePositions
    }

    false
  }

  private def getAllNeighbours(positions : Array[String]) = {
    var neighbours = Set[String]()

    for (position <- positions) {
      for (direction <- Array(leftShift, rightShift, upShift, downShift)) {
        if (!isBlockedMove(position, direction)) {
          neighbours += calculatePositionByShift(position, direction)
        }
      }
    }

    neighbours.diff(positions.toSet)
  }

  private def isBlockedMove(position: String, shift : ShiftDirection) = {
    isBlockedByWall(position, shift.name) || isOutOfBoardMove(position, shift)
  }

  private def isOutOfBoardMove(position : String, shift : ShiftDirection) = {
    !PossiblePositions.pawn.contains(calculatePositionByShift(position, shift))
  }

  private def isBlockedByWall(position: String, direction: String) = {
    val blockingWallPositionShifts = Map[String, Array[ShiftDirection]](
      "up"    -> Array(ShiftDirection(x = -1, y =  0, orientation = "h"), ShiftDirection(x =  0, y =  0, orientation = "h")),
      "down"  -> Array(ShiftDirection(x = -1, y = -1, orientation = "h"), ShiftDirection(x =  0, y = -1, orientation = "h")),
      "left"  -> Array(ShiftDirection(x = -1, y = -1, orientation = "v"), ShiftDirection(x = -1, y =  0, orientation = "v")),
      "right" -> Array(ShiftDirection(x =  0, y = -1, orientation = "v"), ShiftDirection(x =  0, y =  0, orientation = "v"))
    )

    blockingWallPositionShifts(direction).exists((shift) => (game.wallPositions + move.position).contains(calculatePositionByShift(position, shift)))
  }

  private def calculatePositionByShift(position: String, shift : ShiftDirection) = {
    (position.charAt(0) + shift.x).toChar.toString + (position.charAt(1) +  shift.y).toChar.toString + shift.orientation
  }
}
