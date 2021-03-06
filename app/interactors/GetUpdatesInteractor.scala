package interactors

import DAOs.GameDAO
import com.mongodb.casbah.commons.MongoDBObject
import models.User
import org.joda.time.DateTime

class GetUpdatesInteractor(user : User, since : DateTime = null) {
  def call = {
    since match {
      case null => GameDAO.findUserActiveGames(user.id)
      case _ => userGamesSinceDate
    }
  }

  private lazy val userGamesSinceDate = {
    getGames("players._id" -> user.id, "updatedAt" -> MongoDBObject("$gt" -> since))
  }

  private def getGames(conditions : (String, Any)*) = {
    GameDAO.find(MongoDBObject(conditions.toList)).sort(orderBy = MongoDBObject("updatedAt" -> -1)).toList
  }
}
