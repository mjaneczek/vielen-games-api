package interactors

import java.util.ArrayList;
import DAOs.GameDAO
import com.mongodb.WriteConcern
import com.mongodb.casbah.commons.MongoDBObject
import models.{User, Player, Move, Game}
import org.joda.time.DateTime

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

  private def canMove = {
    activePlayer.id == user.id &&
    !game.players.map(p => p.pawnPosition).contains(move.position) &&
    !game.moves.filter((m) => m.moveType == "wall").map((m) => m.position).contains(move.position) &&
    isNotOutOfBoardMove &&
    isValidPawnMove
  }

  private def isNotOutOfBoardMove = {
    val horizontalPositions = Array("a","b","c","d","e","f","g","h","i")
    val verticalPositions = (1 to 9).toArray

    horizontalPositions.contains(move.position.charAt(0).toString) && verticalPositions.contains(move.position.charAt(1).toString.toInt)
  }

  private def isOutOfBoardMove(position : String) = {
    val horizontalPositions = Array("a","b","c","d","e","f","g","h","i")
    val verticalPositions = (1 to 9).toArray

    !horizontalPositions.contains(position.charAt(0).toString) || !position.charAt(1).isDigit || !verticalPositions.contains(position.charAt(1).toString.toInt)
  }

  private def isValidPawnMove = {
    var leftMove = (activePlayer.pawnPosition.charAt(0) - 1).toChar.toString + activePlayer.pawnPosition.charAt(1).toChar.toString
    var rightMove = (activePlayer.pawnPosition.charAt(0) + 1).toChar.toString + activePlayer.pawnPosition.charAt(1).toChar.toString
    var upMove = activePlayer.pawnPosition.charAt(0).toChar.toString + (activePlayer.pawnPosition.charAt(1) + 1).toChar.toString
    var downMove = activePlayer.pawnPosition.charAt(0).toChar.toString + (activePlayer.pawnPosition.charAt(1) - 1).toChar.toString

    var possibleMoves = new ArrayList[String]()

    if(leftMove == nonActivePlayer.pawnPosition) {
      leftMove = (activePlayer.pawnPosition.charAt(0) - 2).toChar.toString + activePlayer.pawnPosition.charAt(1).toChar.toString

      if(isOutOfBoardMove(leftMove)) {
        possibleMoves.add("a" + (activePlayer.pawnPosition.charAt(1) - 1).toChar.toString)
        possibleMoves.add("a" + (activePlayer.pawnPosition.charAt(1) + 1).toChar.toString)
      } else {
        possibleMoves.add(leftMove)
      }
    } else {
      possibleMoves.add(leftMove)
    }

    if(rightMove == nonActivePlayer.pawnPosition) {
      rightMove = (activePlayer.pawnPosition.charAt(0) + 2).toChar.toString + activePlayer.pawnPosition.charAt(1).toChar.toString

      if(isOutOfBoardMove(rightMove)) {
        possibleMoves.add("i" + (activePlayer.pawnPosition.charAt(1) - 1).toChar.toString)
        possibleMoves.add("i" + (activePlayer.pawnPosition.charAt(1) + 1).toChar.toString)
      } else {
        possibleMoves.add(rightMove)
      }
    } else {
      possibleMoves.add(rightMove)
    }

    if(upMove == nonActivePlayer.pawnPosition) {
      upMove = activePlayer.pawnPosition.charAt(0).toChar.toString + (activePlayer.pawnPosition.charAt(1) + 2).toChar.toString

      if(isOutOfBoardMove(upMove)) {
        possibleMoves.add((activePlayer.pawnPosition.charAt(0) - 1).toChar.toString + "9")
        possibleMoves.add((activePlayer.pawnPosition.charAt(0) + 1).toChar.toString + "9")
      } else {
        possibleMoves.add(upMove)
      }
    } else {
      possibleMoves.add(upMove)
    }

    if(downMove == nonActivePlayer.pawnPosition) {
      downMove = activePlayer.pawnPosition.charAt(0).toChar.toString + (activePlayer.pawnPosition.charAt(1) - 2).toChar.toString

      if(isOutOfBoardMove(downMove)) {
        possibleMoves.add((activePlayer.pawnPosition.charAt(0) - 1).toChar.toString + "1")
        possibleMoves.add((activePlayer.pawnPosition.charAt(0) + 1).toChar.toString + "1")
      } else {
        possibleMoves.add(downMove)
      }
    } else {
      possibleMoves.add(downMove)
    }

    possibleMoves contains move.position
  }
}