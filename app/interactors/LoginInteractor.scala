package interactors

import DAOs.UserDAO
import com.mongodb.casbah.commons.MongoDBObject
import com.restfb.{FacebookClient}
import models.User

class LoginInteractor(facebookClient: FacebookClient) {
  def call : User = {
    findUserByProviderId.getOrElse { UserDAO.insert(newUser); newUser }
  }

  private def newUser = {
    User(name = fbUser.getName, providerId = fbUser.getId, authenticateToken = generateAuthToken)
  }

  private def fbUser = {
    facebookClient.fetchObject("me", classOf[com.restfb.types.User])
  }

  private def generateAuthToken = {
    java.util.UUID.randomUUID.toString
  }

  private def findUserByProviderId = {
    UserDAO.findOne(MongoDBObject("providerId" -> fbUser.getId))
  }
}
