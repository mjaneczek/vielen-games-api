package models

import java.util.Date
import com.mongodb.casbah.Imports._

case class User
(
  id: ObjectId = new ObjectId,
  name: String,
  providerId: String,
  authenticateTokens: List[String] = List.empty
)