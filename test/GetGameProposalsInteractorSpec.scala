import DAOs.{GameDAO, GameProposalDAO}
import interactors.GetGameProposalsInteractor
import models.{Player, Game, User, GameProposal}

class GetGameProposalsInteractorSpec extends InteractorSpec {
  val user = User(name = "Test", providerId = "1234", authenticateToken = "test")
  val secondUser = User(name = "Test 2", providerId = "1234", authenticateToken = "test")

  val existingGameProposals = List(GameProposal(users = List(secondUser)), GameProposal(users = List(secondUser)))

  def player(user : User) = {
    Player(id = user.id, name = user.name, providerId = user.providerId, team = "team_1", pawnPosition = "e1", wallsLeft = 10)
  }

  "Get game proposals interactor" should {

    "returns all game proposal" in {
      //before
      existingGameProposals.foreach(gameProposal => {
        GameProposalDAO.insert(gameProposal)
      })

      val interactor = new GetGameProposalsInteractor(user)
      val gameProposals = interactor.call
      gameProposals must contain(existingGameProposals(0), existingGameProposals(1))
    }

    "skip game proposal if user has already 2 games with proposing user" in {
      GameDAO.insert(Game(players = List(player(user), player(secondUser))))
      GameDAO.insert(Game(players = List(player(user), player(secondUser))))

      val interactor = new GetGameProposalsInteractor(user)
      val gameProposals = interactor.call
      gameProposals must haveSize(0)
    }

  }
}
