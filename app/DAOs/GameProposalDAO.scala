package DAOs

import models.GameProposal
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._

object GameProposalDAO extends ModelCompanion[GameProposal, ObjectId] {
  val dao = new SalatDAO[GameProposal, ObjectId](defineCollection("game_proposals")) { }
}