import DAOs.{GameDAO}
import interactors.{CreateMoveInteractor}
import models.{Move, Player, Game}

class CreateMoveInteractorSpec extends InteractorSpec {
  val activePlayer = Player(name = "Test", providerId = "1234", team = "team_1", pawnPosition = "e1", wallsLeft = 10)
  val player = Player(name = "Test 2", providerId = "1234", team = "team_2", pawnPosition = "e9", wallsLeft = 10)

  val game = Game(players = List(activePlayer, player), activeTeam = "team_1")
  val move = Move(position = "e2", moveType = "pawn")

  val interactor = new CreateMoveInteractor(game, move)

  "Create move interactor" should {

    "creates new move in game" in {
      GameDAO.insert(game)
      interactor.call

      lastGame.moves.head must beEqualTo(move)
      lastGame.activeTeam must beEqualTo("team_2")
    }

    "changes pawn position" in {
      lastGame.players.head.pawnPosition must beEqualTo("e2")
    }

    "calculates walls left" in {
      new CreateMoveInteractor(game, Move(moveType = "wall", position = "he3")).call
      lastGame.players.head.wallsLeft must beEqualTo(9)
    }

  }

  def lastGame = {
    GameDAO.findAll().toList.last
  }
}
