import interactors.CreateMoveInteractor
import models.Move

class SimplePawnMoveSpec extends MoveValidationSpec {
  "Simple pawn move validation" should {
    "validate normal moves by one position" in {
      activePlayer.pawnPosition = "e5"
      assertValidPawnMoves("e4", "e6", "d5", "f5")
    }

    "returns false for zero length move" in {
      activePlayer.pawnPosition = "e5"
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "e5")).call must beFalse
    }

    "not allow for out of board moves" in {
      activePlayer.pawnPosition = "i5"
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "j5")).call must beFalse
    }
  }
}