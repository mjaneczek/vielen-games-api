import DAOs.GameDAO
import interactors.CreateMoveInteractor
import models.Move

class CreateMoveInteractorSpec extends MoveValidationSpec {
  "Create move interactor" should {

    "creates new move in game" in {
      GameDAO.insert(game)
      new CreateMoveInteractor(game, activeUser, Move(position = "e2", moveType = "pawn")).call

      lastGame.moves.head must beEqualTo(Move(position = "e2", moveType = "pawn"))
      lastGame.activeTeam must beEqualTo("team_2")
    }

    "changes pawn position" in {
      lastGame.players.head.pawnPosition must beEqualTo("e2")
    }

    "calculates walls left" in {
      new CreateMoveInteractor(game, activeUser, Move(moveType = "wall", position = "a1h")).call
      lastGame.players.head.wallsLeft must beEqualTo(9)
    }

    "checks is your turn" in {
      new CreateMoveInteractor(game.copy(activeTeam = "team_2"), activeUser, Move(moveType = "pawn", position = "e9")).call must beEqualTo(false)
    }

    "checks winning move" in {
      activePlayer.pawnPosition = "a8"
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "a9")).call
      lastGame.winner must beEqualTo(activePlayer)

      opponentPlayer.pawnPosition = "a2"
      new CreateMoveInteractor(game.copy(activeTeam = "team_2"), opponentUser, Move(moveType = "pawn", position = "a1")).call
      lastGame.winner must beEqualTo(opponentPlayer)

      lastGame.activeTeam must beNull
    }
  }

  def lastGame = {
    GameDAO.findAll().toList.last
  }
}
