import DAOs.{GameDAO}
import interactors.{CreateMoveInteractor}
import models.{User, Move, Player, Game}

class CreateMoveInteractorSpec extends InteractorSpec {
  val user = User(name = "Test", providerId = "1234")
  val activePlayer = Player(id = user.id, name = "Test", providerId = "1234", team = "team_1", pawnPosition = "e1", wallsLeft = 10)

  val secondUser = User(name = "Test 2", providerId = "1234")
  val player = Player(id = secondUser.id, name = "Test 2", providerId = "1234", team = "team_2", pawnPosition = "e9", wallsLeft = 10)

  val game = Game(players = List(activePlayer, player), activeTeam = "team_1")
  val move = Move(position = "e2", moveType = "pawn")

  val interactor = new CreateMoveInteractor(game, user, move)

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
      new CreateMoveInteractor(game, user, Move(moveType = "wall", position = "he3")).call
      lastGame.players.head.wallsLeft must beEqualTo(9)
    }

    "checks winning move" in {
      new CreateMoveInteractor(game, user, Move(moveType = "pawn", position = "e9")).call
      lastGame.winner must beEqualTo(activePlayer)

      new CreateMoveInteractor(game.copy(activeTeam = "team_2"), secondUser, Move(moveType = "pawn", position = "e1")).call
      lastGame.winner must beEqualTo(player)

      lastGame.activeTeam must beNull
    }

    "checks is your turn" in {
      val result = new CreateMoveInteractor(game.copy(activeTeam = "team_2"), user, Move(moveType = "pawn", position = "e9")).call
      result must beEqualTo(false)
    }

  }

  def lastGame = {
    GameDAO.findAll().toList.last
  }
}
