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

  "Pawn jump blocked by wall validation" should {
    opponentPlayer.pawnPosition = "e5"

    "validates jumping north over opponent" in {
      setGameWalls("e4h")
      activePlayer.pawnPosition = "e4"

      assertValidPawnMoves("e3", "d4", "f4")
    }

    "validates jumping south over opponent" in {
      setGameWalls("e5h")
      activePlayer.pawnPosition = "f6"

      assertValidPawnMoves("f7", "e6", "g6")
    }

    "validates jumping east over opponent" in {
      setGameWalls("d5v")
      activePlayer.pawnPosition = "d5"

      assertValidPawnMoves("c5", "d4", "d6")
    }

    "validates jumping west over opponent" in {
      setGameWalls("e4v")
      activePlayer.pawnPosition = "f5"

      assertValidPawnMoves("g5", "f4", "f6")
    }
  }
}