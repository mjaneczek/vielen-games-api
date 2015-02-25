package interactors

import DAOs.{GameDAO, GameProposalDAO}
import models.{Player, Game, GameProposal, User}

class StartGameInteractor(user: User, gameProposal: GameProposal) {
  def call = {
    GameProposalDAO.remove(gameProposal)
    GameDAO.insert(gameFromProposal)
    gameFromProposal
  }

  private val gameFromProposal = {
    Game(users = getPlayers)
  }

  private def getPlayers = {
    List(createPlayer(user, "team_1"), createPlayer(proposalUser, "team_2"))
  }

  private def createPlayer(user : User, teamName : String) = {
    Player(id = user.id, name = user.name, team = teamName)
  }

  private def proposalUser = {
    gameProposal.users.head
  }
}