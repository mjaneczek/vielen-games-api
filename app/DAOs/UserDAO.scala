package DAOs

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._
import models.User

object UserDAO extends ModelCompanion[User, ObjectId] {
  def collection = MongoConnection()(DatabaseName)("users")
  val dao = new SalatDAO[User, ObjectId](collection) {}
}