package interactors

import DAOs.{GameDAO, GameProposalDAO}
import models.{Player, Game, User}
import org.bson.types.ObjectId

import scala.util.Random

class StartGameInteractor(user: User, gameProposalId: String) {
  def call = {
    GameProposalDAO.remove(gameProposal)
    GameDAO.insert(gameFromProposal)
    gameFromProposal
  }

  private val gameFromProposal = {
    Game(players = getPlayers, activeTeam = randomTeam)
  }

  private lazy val gameProposal = {
    GameProposalDAO.findOneById(new ObjectId(gameProposalId)).get
  }

  private def getPlayers = {
    List(createPlayer(user, "team_1"), createPlayer(proposalUser, "team_2"))
  }

  private def createPlayer(user : User, teamName : String) = {
    Player(id = user.id, name = user.name, team = teamName, providerId = user.providerId, pawnPosition = startPosition(teamName), wallsLeft = 10)
  }

  def startPosition(team : String) = {
    team match {
      case "team_1" => "e1"
      case "team_2" => "e9"
    }
  }

  def randomTeam = {
    Random.shuffle(List("team_1", "team_2")).head
  }

  private def proposalUser = {
    gameProposal.users.head
  }
}