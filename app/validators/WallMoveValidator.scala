package validators

import models.{Move, Game}

class WallMoveValidator(game : Game, move : Move) {
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
