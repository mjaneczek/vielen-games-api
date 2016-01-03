package interactors

import DAOs.{GameDAO, GameProposalDAO}
import com.mongodb.casbah.commons.MongoDBObject
import models.{Player, GameProposal, User}

class GetGameProposalsInteractor(user : User) {
  def call = {
    filteredGameProposals
  }

  private def filteredGameProposals = {
    gameProposals.filter(gameProposal => isMyProposal(gameProposal) || alreadyGamesCount(gameProposal.users.head) < 2)
  }

  private def gameProposals = {
    GameProposalDAO.findAll().sort(orderBy = newest).toList
  }

  private val newest = {
    MongoDBObject("created_at" -> -1)
  }

  private lazy val userGames = {
    GameDAO.findUserActiveGames(user.id)
  }

  private def isMyProposal(proposal : GameProposal) = {
    proposal.users.head == user
  }

  def alreadyGamesCount(user : User) = {
    userGames.count(game => game.players.map(player => player.id).contains(user.id))
  }
}
