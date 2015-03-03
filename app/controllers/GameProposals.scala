package controllers

import models.User
import play.api.mvc._
import serializers.GameProposalSerializer
import interactors.{StartGameInteractor, DeleteGameProposalInteractor, CreateGameProposalInteractor, GetGameProposalsInteractor}

object GameProposals extends Controller {

  def index = APIAction { request =>
    renderGameProposals(request.currentUser)
  }

  def create = APIAction { request =>
    new CreateGameProposalInteractor(request.currentUser).call
    renderGameProposals(request.currentUser)
  }

  def startGame(id: String) = APIAction { request =>
    new StartGameInteractor(request.currentUser, id).call
    renderGameProposals(request.currentUser)
  }

  def delete(id: String, userId: String) = APIAction { request =>
    new DeleteGameProposalInteractor(request.currentUser, id).call
    renderGameProposals(request.currentUser)
  }

  def renderGameProposals(user : User) = {
    val gameProposals = new GetGameProposalsInteractor(user).call
    Ok(new GameProposalSerializer(gameProposals).toJson)
  }
}