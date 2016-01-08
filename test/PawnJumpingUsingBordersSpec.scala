class PawnJumpingUsingBordersSpec extends MoveValidationSpec {
  "Pawn jumping using borders validation" should {
    "allows jumping left or right over opponent when north border is behind" in {
      activePlayer.pawnPosition = "e8"
      opponentPlayer.pawnPosition = "e9"

      assertValidPawnMoves("e7", "f8", "d8", "d9", "f9")
    }

    "allows jumping left or right over opponent when south border is behind" in {
      activePlayer.pawnPosition = "e2"
      opponentPlayer.pawnPosition = "e1"

      assertValidPawnMoves("e3", "d2", "f2", "d1", "f1")
    }

    "allows jumping left or right over opponent when east border is behind" in {
      activePlayer.pawnPosition = "b5"
      opponentPlayer.pawnPosition = "a5"

      assertValidPawnMoves("c5", "b4", "b6", "a4", "a6")
    }

    "allows jumping left or right over opponent when west border is behind" in {
      activePlayer.pawnPosition = "h5"
      opponentPlayer.pawnPosition = "i5"

      assertValidPawnMoves("g5", "h4", "h6", "i4", "i6")
    }
  }
}