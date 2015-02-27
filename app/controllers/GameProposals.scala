package controllers

import DAOs.UserDAO
import com.mongodb.casbah.commons.MongoDBObject
import models.User
import play.api.mvc._
import serializers.GameProposalSerializer
import interactors.{StartGameInteractor, DeleteGameProposalInteractor, CreateGameProposalInteractor, GetGameProposalsInteractor}

object GameProposals extends Controller {

  def index = Action { request =>
    renderGameProposals(currentUser(request))
  }

  def create = Action { request =>
    new CreateGameProposalInteractor(currentUser(request)).call
    renderGameProposals(currentUser(request))
  }

  def startGame(id: String) = Action { request =>
    new StartGameInteractor(currentUser(request), id).call
    renderGameProposals(currentUser(request))
  }

  def delete(id: String, userId: String) = Action { request =>
    new DeleteGameProposalInteractor(currentUser(request), id).call
    renderGameProposals(currentUser(request))
  }

  def renderGameProposals(user : User) = {
    val gameProposals = new GetGameProposalsInteractor(user).call
    Ok(new GameProposalSerializer(gameProposals).toJson)
  }

  def currentUser(request : Request[AnyContent]) = {
    val authToken = request.headers.get("X-Auth-Token").get
    UserDAO.findOne(MongoDBObject("authenticateToken" -> authToken)).get
  }
}