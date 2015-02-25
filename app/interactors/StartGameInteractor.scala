package interactors

import DAOs.{GameDAO, GameProposalDAO}
import models.{Player, Game, GameProposal, User}
import se.radley.plugin.salat.Binders.ObjectId

class StartGameInteractor(user: User, gameProposalId: String) {
  def call = {
    GameProposalDAO.remove(gameProposal)
    GameDAO.insert(gameFromProposal)
    gameFromProposal
  }

  private val gameFromProposal = {
    Game(users = getPlayers)
  }

  private lazy val gameProposal = {
    GameProposalDAO.findOneById(new ObjectId(gameProposalId)).get
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