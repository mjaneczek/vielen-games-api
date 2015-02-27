package interactors

import DAOs.GameDAO
import com.mongodb.casbah.commons.MongoDBObject
import models.User
import org.joda.time.DateTime

class GetUpdatesInteractor(user : User, since : DateTime = null) {
  def call = {
    since match {
      case null => userActiveGames
      case _ => userGames
    }
  }

  private lazy val userGames = {
    GameDAO.find(MongoDBObject("players._id" -> user.id)).toList
  }

  private lazy val userActiveGames = {
    GameDAO.find(MongoDBObject("players._id" -> user.id, "winner" -> null)).toList
  }
}
