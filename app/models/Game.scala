package models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime

case class Game
(
  id: ObjectId = new ObjectId,
  users: List[Player],
  winnerId: String = null,
  created_at: DateTime = DateTime.now()
)