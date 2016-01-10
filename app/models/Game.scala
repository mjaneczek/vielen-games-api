package models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime

case class Game
(
  id: ObjectId = new ObjectId,
  players: List[Player],
  winner: Player = null,
  var moves: List[Move] = List.empty,
  activeTeam: String = null,
  createdAt: DateTime = DateTime.now(),
  updatedAt: DateTime = DateTime.now()
)
{
  def activePlayer = {
    players.find(player => player.team == activeTeam).get
  }

  def opponent = {
    players.filterNot(player => player == activePlayer).head
  }

  def wallPositions = {
    moves.filter((m) => m.moveType == "wall").map((m) => m.position)
  }
}