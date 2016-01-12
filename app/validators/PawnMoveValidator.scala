package validators

import models.{ShiftDirection, Game, Move}
import scala.collection.mutable.ArrayBuffer

class PawnMoveValidator(game : Game, move : Move) extends MoveValidator(game, move) {
  val validPositions = ArrayBuffer.empty[String]

  def isValidMove = {
    for(shift <- possibleShifts)
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

  private def allowPositionUnlessBlocked(position : String, shift : ShiftDirection) = {
    if(!isBlockedMove(position, shift))
      validPositions += calculatePositionByShift(position, shift)
  }

  private def isPositionTakenByOpponent(shift : ShiftDirection) = {
    calculatePositionByShift(activePlayer.pawnPosition, shift) == opponent.pawnPosition
  }

  private def isWallOrBorderBehindOpponent(shift : ShiftDirection) = {
    isBlockedMove(opponent.pawnPosition, shift)
  }
}
