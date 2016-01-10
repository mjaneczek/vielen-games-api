package validators

import models.{ShiftDirection, Game, Move}
import utils.PossiblePositions
import scala.collection.mutable.ArrayBuffer

class PawnMoveValidator(game : Game, move : Move) {
  lazy val activePlayer = { game.activePlayer }
  lazy val opponent = { game.opponent }
  val validPositions = ArrayBuffer.empty[String]

  val leftShift  =  ShiftDirection(name = "left",  x = -1, y =  0)
  val rightShift =  ShiftDirection(name = "right", x =  1, y =  0)
  val upShift    =  ShiftDirection(name = "up",    x =  0, y =  1)
  val downShift  =  ShiftDirection(name = "down",  x =  0, y = -1)

  def isValidMove = {
    for(shift <- Array(leftShift, rightShift, upShift, downShift))
      calculatePossiblePositions(shift)

    validPositions contains move.position
  }

  private def calculatePossiblePositions(shift : ShiftDirection) : Unit = {
    if(isBlockedMove(activePlayer.pawnPosition, shift))
      return

    if (isPositionTakenByOpponent(shift)) {
      if(isWallOrBorderBehindOpponent(shift)) {
        handleSideJumping(shift)
      } else {
        handleNormalJump(shift)
      }
    } else {
      handleNormalMove(shift)
    }
  }

  private def handleNormalMove(shift : ShiftDirection) = {
    allowPositionUnlessBlocked(activePlayer.pawnPosition, shift)
  }

  private def handleNormalJump(shift : ShiftDirection) = {
    allowPositionUnlessBlocked(opponent.pawnPosition, shift)
  }

  private def handleSideJumping(shift : ShiftDirection) = {
    if(shift.x == 0) {
      allowPositionUnlessBlocked(opponent.pawnPosition, leftShift)
      allowPositionUnlessBlocked(opponent.pawnPosition, rightShift)
    } else {
      allowPositionUnlessBlocked(opponent.pawnPosition, upShift)
      allowPositionUnlessBlocked(opponent.pawnPosition, downShift)
    }
  }

  private def isPositionTakenByOpponent(shift : ShiftDirection) = {
    calculatePositionByShift(activePlayer.pawnPosition, shift) == opponent.pawnPosition
  }

  private def isWallOrBorderBehindOpponent(shift : ShiftDirection) = {
    isBlockedMove(opponent.pawnPosition, shift)
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

    blockingWallPositionShifts(direction).exists((shift) => game.wallPositions.contains(calculatePositionByShift(position, shift)))
  }

  private def allowPositionUnlessBlocked(position : String, shift : ShiftDirection) = {
    if(!isBlockedMove(position, shift))
      validPositions += calculatePositionByShift(position, shift)
  }

  private def calculatePositionByShift(position: String, shift : ShiftDirection) = {
    (position.charAt(0) + shift.x).toChar.toString + (position.charAt(1) +  shift.y).toChar.toString + shift.orientation
  }
}
