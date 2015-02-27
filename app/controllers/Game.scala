package controllers

import DAOs.{UserDAO, GameDAO}
import com.mongodb.casbah.commons.MongoDBObject
import interactors.CreateMoveInteractor
import models.Move
import play.api.libs.json.{JsValue, JsObject}
import play.api.mvc._
import se.radley.plugin.salat.Binders.ObjectId

object Game extends Controller {

  def addMove(game_id: String) = Action(parse.tolerantJson) { request =>
    val params = request.body.as[JsObject].fields.toMap

    val game = GameDAO.findOneById(new ObjectId(game_id)).get

    game.synchronized {
      val move = Move(moveType = params("move_type").as[String], position = params("position").as[String])
      new CreateMoveInteractor(game, currentUser(request), move).call
    }

    Ok
  }

  def currentUser(request : Request[JsValue]) = {
    val authToken = request.headers.get("X-Auth-Token").get
    UserDAO.findOne(MongoDBObject("authenticateTokens" -> authToken)).get
  }
}