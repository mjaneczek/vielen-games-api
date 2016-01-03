package interactors

import DAOs.{GameProposalDAO}
import com.mongodb.casbah.commons.MongoDBObject
import models.{User}
import org.bson.types.ObjectId

class DeleteGameProposalInteractor(user: User, id: String) {
  def call = {
    GameProposalDAO.remove(MongoDBObject("_id" -> new ObjectId(id), "users._id" -> user.id))
  }
}
