package models

import java.util.Date
import com.mongodb.casbah.Imports._

case class User
(
  id: ObjectId = new ObjectId,
  name: String,
  avatar_url: Option[String] = None,
  created_at: Option[Date] = None
)