package interactors

import DAOs.GameProposalDAO
import com.mongodb.casbah.commons.MongoDBObject

class GetGameProposalsInteractor {
  def call = {
    GameProposalDAO.findAll().sort(orderBy = newest).toList
  }

  private val newest = {
    MongoDBObject("created_at" -> -1)
  }
}
