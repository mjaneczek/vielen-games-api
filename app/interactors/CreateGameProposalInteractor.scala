package interactors

import DAOs.GameProposalDAO
import com.mongodb.casbah.commons.MongoDBObject
import models.{GameProposal, User}

class CreateGameProposalInteractor(user: User) {
  def call = {
    GameProposalDAO.insert(newGameProposal)
    deleteOldestUserGameProposalIfMoreThanThree()
    newGameProposal
  }

  private val newGameProposal = {
    new GameProposal(users = List(user))
  }

  private def deleteOldestUserGameProposalIfMoreThanThree() = {
    if(currentUserGameProposal.size > 3) {
      GameProposalDAO.remove(currentUserGameProposal.head)
    }
  }

  private lazy val currentUserGameProposal = {
    GameProposalDAO.find(MongoDBObject("users._id" -> user.id)).toList
  }
}