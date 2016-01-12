import models.Move

class PawnMovesBlockedByWallsSpec extends MoveValidationSpec {
  "Pawn moves blocked by walls validation" should {
    setGameWalls("c6v", "g6h")

    "validates jumping over north wall" in {
      activePlayer.pawnPosition = "g6"
      assertValidPawnMoves("g5", "h6", "f6")

      activePlayer.pawnPosition = "h6"
      assertValidPawnMoves("h5", "i6", "g6")
    }

    "validates jumping over south wall" in {
      activePlayer.pawnPosition = "g7"
      assertValidPawnMoves("g8", "h7", "f7")

      activePlayer.pawnPosition = "h7"
      assertValidPawnMoves("h8", "i7", "g7")
    }

    "validates jumping over west wall" in {
      activePlayer.pawnPosition = "d6"
      assertValidPawnMoves("e6", "d5", "d7")

      activePlayer.pawnPosition = "d7"
      assertValidPawnMoves("e7", "d6", "d8")
    }

    "validates jumping over east wall" in {
      activePlayer.pawnPosition = "c6"
      assertValidPawnMoves("b6", "c5", "c7")

      activePlayer.pawnPosition = "c7"
      assertValidPawnMoves("b7", "c6", "c8")
    }
  }
}