package models

import org.joda.time.DateTime
import com.mongodb.casbah.Imports._

case class GameProposal
(
  id: ObjectId = new ObjectId,
  users: List[User] = List.empty,
  created_at: DateTime = DateTime.now()
)