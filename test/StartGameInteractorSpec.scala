import DAOs.{GameDAO, GameProposalDAO}
import interactors.StartGameInteractor
import models.{GameProposal, User}
import org.specs2.specification.BeforeExample

class StartGameInteractorSpec extends InteractorSpec with BeforeExample {
  val proposalUser = User(name = "Proposal user", providerId = "1234", authenticateToken = "test")
  val gameProposal = GameProposal(users = List(proposalUser))

  def before = {
    GameProposalDAO.insert(gameProposal)
  }

  val joiningUser = User(name = "User who wanna join", providerId = "1234", authenticateToken = "test")
  def interactor = new StartGameInteractor(joiningUser, gameProposal.id.toString)

  "Start game interactor" should {

    "creates new game with proper teams" in {
      val game = interactor.call
      GameDAO.count() must beEqualTo(1)

      game.users must haveSize(2)
      game.users.head.team must beEqualTo("team_1")
      game.users.last.team must beEqualTo("team_2")
    }

    "removes game proposal" in {
      interactor.call
      GameProposalDAO.count() must beEqualTo(0)
    }
  }
}
