package validators

import models.{Move, Game, ShiftDirection}
import utils.PossiblePositions

class MoveValidator(game : Game, move : Move) {
  lazy val activePlayer = { game.activePlayer }
  lazy val opponent = { game.opponent }

  val leftShift  =  ShiftDirection(name = "left",  x = -1, y =  0)
  val rightShift =  ShiftDirection(name = "right", x =  1, y =  0)
  val upShift    =  ShiftDirection(name = "up",    x =  0, y =  1)
  val downShift  =  ShiftDirection(name = "down",  x =  0, y = -1)

  var possibleShifts = Array(leftShift, rightShift, upShift, downShift)

  protected def isBlockedMove(position: String, shift : ShiftDirection, walls: List[String] = game.wallPositions) = {
    isBlockedByWall(position, shift.name, walls) || isOutOfBoardMove(position, shift)
  }

  protected def isOutOfBoardMove(position : String, shift : ShiftDirection) = {
    !PossiblePositions.pawn.contains(calculatePositionByShift(position, shift))
  }

  protected def isBlockedByWall(position: String, direction: String, walls: List[String]) = {
    val blockingWallPositionShifts = Map[String, Array[ShiftDirection]](
      "up"    -> Array(ShiftDirection(x = -1, y =  0, orientation = "h"), ShiftDirection(x =  0, y =  0, orientation = "h")),
      "down"  -> Array(ShiftDirection(x = -1, y = -1, orientation = "h"), ShiftDirection(x =  0, y = -1, orientation = "h")),
      "left"  -> Array(ShiftDirection(x = -1, y = -1, orientation = "v"), ShiftDirection(x = -1, y =  0, orientation = "v")),
      "right" -> Array(ShiftDirection(x =  0, y = -1, orientation = "v"), ShiftDirection(x =  0, y =  0, orientation = "v"))
    )

    blockingWallPositionShifts(direction).exists((shift) => walls.contains(calculatePositionByShift(position, shift)))
  }

  protected def calculatePositionByShift(position: String, shift : ShiftDirection) = {
    (position.charAt(0) + shift.x).toChar.toString + (position.charAt(1) +  shift.y).toChar.toString + shift.orientation
  }
}
