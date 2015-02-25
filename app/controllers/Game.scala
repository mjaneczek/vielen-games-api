package controllers

import DAOs.GameDAO
import interactors.CreateMoveInteractor
import models.Move
import play.api.libs.json.JsObject
import play.api.mvc._
import se.radley.plugin.salat.Binders.ObjectId

object Game extends Controller {

  def addMove(game_id: String) = Action(parse.tolerantJson) { request =>
    val params = request.body.as[JsObject].fields.toMap

    val game = GameDAO.findOneById(new ObjectId(game_id)).get
    val move = Move(moveType = params("move_type").as[String], position = params("position").as[String])

    new CreateMoveInteractor(game, move).call

    Ok
  }
}