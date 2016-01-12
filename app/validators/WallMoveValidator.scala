package validators

import models.{Move, Game}

class WallMoveValidator(game : Game, move : Move) extends MoveValidator(game, move) {
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

  def isCompletelyBlockingPawn = {
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
        if (!isBlockedMove(position, direction, game.wallPositions :+ move.position)) {
          neighbours += calculatePositionByShift(position, direction)
        }
      }
    }

    neighbours.diff(positions.toSet)
  }
}
