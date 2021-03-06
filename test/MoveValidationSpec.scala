import interactors.CreateMoveInteractor
import models.{Player, Move, Game, User}
import utils.PossiblePositions

trait MoveValidationSpec extends InteractorSpec {
  var activeUser = User(name = "Active user", providerId = "1234")
  var opponentUser = User(name = "Opponent user", providerId = "1234")

  var activePlayer = Player(id = activeUser.id, name = "Active player", providerId = "1234", team = "team_1", pawnPosition = "e1", wallsLeft = 10)
  var opponentPlayer = Player(id = opponentUser.id, name = "Opponent player", providerId = "1234", team = "team_2", pawnPosition = "e9", wallsLeft = 10)

  var game = Game(players = List(activePlayer, opponentPlayer), activeTeam = "team_1")

  def setGameWalls(wallPositions : String*) = {
    game.moves = wallPositions.map(p => Move(position = p, moveType = "wall")).toList
  }

  def assertValidPawnMoves(moves : String*) = {
    PossiblePositions.pawn.forall((position) => {
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = position)).call == (moves contains position)
    }) must beTrue
  }

  def assertValidWallMoves(moves : String*) = {
    assertWallMoves(moves, expectedCondition = true)
  }

  def assertInvalidWallMoves(moves : String*) = {
    assertWallMoves(moves, expectedCondition = false)
  }

  private def assertWallMoves(moves : Seq[String], expectedCondition : Boolean) = {
    moves.forall((p) => new CreateMoveInteractor(game, activeUser, Move(moveType = "wall", position = p)).call == expectedCondition) must beTrue
  }
}
