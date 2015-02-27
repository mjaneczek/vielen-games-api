import DAOs.{GameDAO, GameProposalDAO}
import interactors.StartGameInteractor
import models.{GameProposal, User}
import org.specs2.specification.BeforeExample

class StartGameInteractorSpec extends InteractorSpec with BeforeExample {
  val proposalUser = User(name = "Proposal user", providerId = "1234")
  val gameProposal = GameProposal(users = List(proposalUser))

  def before = {
    GameProposalDAO.insert(gameProposal)
  }

  val joiningUser = User(name = "User who wanna join", providerId = "1234")
  def interactor = new StartGameInteractor(joiningUser, gameProposal.id.toString)

  "Start game interactor" should {

    "creates new game with proper teams" in {
      val game = interactor.call
      GameDAO.count() must beEqualTo(1)

      game.activeTeam must beEqualTo("team_1").or(beEqualTo("team_2"))
      game.players must haveSize(2)

      game.players.head.team must beEqualTo("team_1")
      game.players.head.pawnPosition must beEqualTo("e1")

      game.players.last.team must beEqualTo("team_2")
      game.players.last.pawnPosition must beEqualTo("e9")
    }

    "removes game proposal" in {
      interactor.call
      GameProposalDAO.count() must beEqualTo(0)
    }
  }
}
