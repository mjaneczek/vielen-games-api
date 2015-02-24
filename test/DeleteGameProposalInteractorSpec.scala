import DAOs.GameProposalDAO
import interactors.DeleteGameProposalInteractor
import models.{GameProposal, User}
import org.specs2.specification.BeforeExample

class DeleteGameProposalInteractorSpec extends InteractorSpec with BeforeExample {
  val user = User(name = "Test", providerId = "1234", authenticateToken = "test")
  val gameProposal = GameProposal(users = List(user))

  def before = {
    GameProposalDAO.insert(gameProposal)
  }

  val interactor = new DeleteGameProposalInteractor(user, gameProposal.id.toString)

  "Delete game proposals interactor" should {

    "deletes game proposal by id" in {
      interactor.call

      GameProposalDAO.count() must beEqualTo(0)
    }

  }
}
