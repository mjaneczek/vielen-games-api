package validators

import models.{Move, Game}

class WallMoveValidator(game : Game, move : Move) {
  def isValidMove = {
    game.activePlayer.wallsLeft > 0 && move.position.matches("[a-h][1-8][vh]") && !game.wallPositions.contains(move.position) &&
      !game.wallPositions.map((p) => p.take(2).toString).contains(move.position.take(2).toString) && !isPartiallyOverlapping
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
}
