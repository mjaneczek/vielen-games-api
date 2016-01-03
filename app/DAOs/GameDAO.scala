package DAOs

import models.{Game}
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._

object GameDAO extends ModelCompanion[Game, ObjectId] {
  val dao = new MySalatDAO[Game, ObjectId](defineCollection("games")) { }

  def findUserActiveGames(userId : ObjectId) = {
    GameDAO.find(MongoDBObject("players._id" -> userId, "winner" -> null)).sort(orderBy = MongoDBObject("updatedAt" -> -1)).toList
  }
}