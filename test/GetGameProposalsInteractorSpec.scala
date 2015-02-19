import DAOs.GameProposalDAO
import interactors.GetGameProposalsInteractor
import models.{User, GameProposal}
import org.specs2.specification.BeforeExample

class GetGameProposalsInteractorSpec extends InteractorSpec with BeforeExample {
  val interactor = new GetGameProposalsInteractor()

  val user = User(name = "Test")
  val existingGameProposals = List(GameProposal(users = List(user)), GameProposal())

  def before = {
    existingGameProposals.foreach(gameProposal => {
      GameProposalDAO.insert(gameProposal)
    })
  }

  "Get game proposals interactor" should {

    "returns all game proposal" in {
      val gameProposals = interactor.call
      gameProposals must contain(existingGameProposals(0), existingGameProposals(1))
    }

  }
}
