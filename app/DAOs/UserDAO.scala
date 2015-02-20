package DAOs

import com.mongodb.casbah.Imports._
import com.novus.salat.dao._
import models.User

object UserDAO extends ModelCompanion[User, ObjectId] {
  val dao = new SalatDAO[User, ObjectId](defineCollection("users")) {}
}