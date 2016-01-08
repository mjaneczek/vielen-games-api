class SimplePawnJumpingSpec extends MoveValidationSpec {
  "Simple pawn jumping validation" should {
    opponentPlayer.pawnPosition = "e5"

    "allows jumping north over opponent" in {
      activePlayer.pawnPosition = "e4"
      assertValidPawnMoves("e3", "d4", "f4", "e6")
    }

    "allows jumping south over opponent" in {
      activePlayer.pawnPosition = "e6"
      assertValidPawnMoves("e7", "d6", "f6", "e4")
    }

    "allows jumping east over opponent" in {
      activePlayer.pawnPosition = "d5"
      assertValidPawnMoves("c5", "d4", "d6", "f5")
    }

    "allows jumping west over opponent" in {
      activePlayer.pawnPosition = "f5"
      assertValidPawnMoves("g5", "f4", "f6", "d5")
    }
  }
}