package DAOs

import models.GameProposal
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._

object GameProposalDAO extends ModelCompanion[GameProposal, ObjectId] {
  def collection = MongoConnection()(DatabaseName)("game_proposals")
  val dao = new SalatDAO[GameProposal, ObjectId](collection) { }
}