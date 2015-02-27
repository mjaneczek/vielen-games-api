import DAOs.GameDAO
import interactors.GetUpdatesInteractor
import models.{Player, Game, User}
import com.github.nscala_time.time.Imports._

class GetUpdatesInteractorSpec extends InteractorSpec {

  val user = User(name = "Test", providerId = "Test", authenticateToken = "123")
  val player = Player(id = user.id, name = user.name, providerId = user.providerId, team = "team_1", pawnPosition = "e1", wallsLeft = 10 )

  val playerGame = Game(players = List(player))
  val finishedGame = Game(players = List(player), winner = player)
  val otherGame = Game(players = List.empty)


  "Get updates interactor" should {
    "returns all not finished user games when there is not since parameter" in {
      List(playerGame, finishedGame, otherGame).foreach(game => GameDAO.insert(game))

      val interactor = new GetUpdatesInteractor(user)
      val games = interactor.call

      games must contain(playerGame)
    }

    "returns all games (also finished) when specific date" in {
      val interactor = new GetUpdatesInteractor(user, DateTime.yesterday)
      val games = interactor.call
      games must contain(playerGame, finishedGame)
    }

    "returns games since specyfic date" in {
      val interactor = new GetUpdatesInteractor(user, DateTime.nextMonth)
      val games = interactor.call

      games must haveSize(0)
    }
  }
}
