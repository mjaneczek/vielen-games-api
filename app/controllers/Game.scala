package controllers

import DAOs.{UserDAO, GameDAO}
import com.mongodb.casbah.commons.MongoDBObject
import interactors.CreateMoveInteractor
import models.Move
import play.api.libs.json.{JsValue, JsObject}
import play.api.mvc._
import org.bson.types.ObjectId

object Game extends Controller {

  def addMove(game_id: String) = APIAction { request =>
    val game = GameDAO.findOneById(new ObjectId(game_id)).get

    game.synchronized {
      val move = Move(moveType = request.params("move_type").as[String], position = request.params("position").as[String])
      new CreateMoveInteractor(game, request.currentUser, move).call
    }

    Ok
  }
}