package interactors

import DAOs.GameProposalDAO

class GetGameProposalsInteractor {
  def call = {
    GameProposalDAO.findAll().toList
  }
}
