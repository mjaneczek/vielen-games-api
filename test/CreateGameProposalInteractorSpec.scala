import DAOs.GameProposalDAO
import interactors.CreateGameProposalInteractor
import models.User

class CreateGameProposalInteractorSpec extends InteractorSpec {
  val user = User(name = "Test", providerId = "1234")
  val interactor = new CreateGameProposalInteractor(user)

  "Create game proposals interactor" should {

    "creates new game proposal" in {
      val gameProposal = interactor.call
      gameProposal.users must contain(user)

      GameProposalDAO.count() must beEqualTo(1)
    }

    "removes oldest game proposal if more than 3" in {
      for( x <- 1 to 10) { new CreateGameProposalInteractor(user).call }
      GameProposalDAO.count() must beEqualTo(3)
    }

  }
}
