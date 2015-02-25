package models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime

case class Game
(
  id: ObjectId = new ObjectId,
  players: List[Player],
  winner: Player = null,
  moves: List[Move] = List.empty,
  created_at: DateTime = DateTime.now()
)