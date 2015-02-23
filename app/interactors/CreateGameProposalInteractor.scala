package interactors

import DAOs.GameProposalDAO
import models.{GameProposal, User}

class CreateGameProposalInteractor(user: User) {
  def call = {
    GameProposalDAO.insert(newGameProposal)
    newGameProposal
  }

  lazy val newGameProposal = {
    new GameProposal(users = List(user))
  }
}
