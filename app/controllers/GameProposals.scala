package controllers

import play.api.mvc._
import serializers.GameProposalSerializer
import interactors.GetGameProposalsInteractor

object GameProposals extends Controller {

  def index = Action {
    val gameProposals = new GetGameProposalsInteractor().call
    Ok(new GameProposalSerializer(gameProposals).toJson())
  }

//  def create = Action {
//    val user = User(name = "test user")
//    val game_proposal = GameProposal(users = List[User](user))
//    GameProposal.insert(game_proposal)
//  }
}