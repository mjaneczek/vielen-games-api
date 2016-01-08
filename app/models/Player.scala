package models

import com.mongodb.casbah.Imports._

case class Player
(
  id: ObjectId = new ObjectId,
  name: String,
  team: String,
  providerId: String,
  var pawnPosition: String,
  wallsLeft: Integer
)