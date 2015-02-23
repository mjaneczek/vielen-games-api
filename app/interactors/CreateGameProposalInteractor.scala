package interactors

import DAOs.GameProposalDAO
import models.{GameProposal, User}

class CreateGameProposalInteractor(user: User) {
  def call = {
    GameProposalDAO.insert(newGameProposal)
    newGameProposal
  }

  private val newGameProposal = {
    new GameProposal(users = List(user))
  }
}
