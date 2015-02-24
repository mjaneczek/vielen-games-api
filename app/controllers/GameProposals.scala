package controllers

import DAOs.UserDAO
import com.mongodb.casbah.commons.MongoDBObject
import play.api.mvc._
import serializers.GameProposalSerializer
import interactors.{DeleteGameProposalInteractor, CreateGameProposalInteractor, GetGameProposalsInteractor}

object GameProposals extends Controller {

  def index = Action {
    renderGameProposals
  }

  def create = Action { request =>
    new CreateGameProposalInteractor(currentUser(request)).call
    renderGameProposals
  }

  def delete(id: String, userId: String) = Action { request =>
    new DeleteGameProposalInteractor(currentUser(request), id).call
    renderGameProposals
  }

  def renderGameProposals = {
    val gameProposals = new GetGameProposalsInteractor().call
    Ok(new GameProposalSerializer(gameProposals).toJson)
  }

  def currentUser(request : Request[AnyContent]) = {
    val authToken = request.headers.get("X-Auth-Token").get
    UserDAO.findOne(MongoDBObject("authenticateToken" -> authToken)).get
  }
}