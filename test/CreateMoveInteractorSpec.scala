import DAOs.{GameDAO}
import interactors.{CreateMoveInteractor}
import models.{Move, Player, Game}
import org.specs2.specification.BeforeExample

class CreateMoveInteractorSpec extends InteractorSpec with BeforeExample {
  val player = Player(name = "Test", providerId = "1234", team = "team_1", pawnPosition = "e1", wallsLeft = 10)
  val game = Game(players = List(player), activeTeam = "team_1")
  val move = Move(team = player.team, position = "e2", moveType = "pawn")

  def before = {
    GameDAO.insert(game)
  }

  val interactor = new CreateMoveInteractor(game, player, move)

  "Create move interactor" should {

    "creates new move in game" in {
      interactor.call
      lastGame.moves.head must beEqualTo(move)
      lastGame.activeTeam must beEqualTo("team_2")
    }

  }

  lazy val lastGame = {
    GameDAO.findAll().toList.last
  }
}
