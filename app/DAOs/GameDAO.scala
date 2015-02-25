package DAOs

import models.Game
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._

object GameDAO extends ModelCompanion[Game, ObjectId] {
  val dao = new SalatDAO[Game, ObjectId](defineCollection("games")) { }
}