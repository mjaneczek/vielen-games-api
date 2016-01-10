import models.Move

class PawnSideJumpingSpec extends MoveValidationSpec {
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

  "Pawn jumping using wall behind opponent validation" should {
    game.moves = List(
      Move(moveType = "wall", position = "c4h"),
      Move(moveType = "wall", position = "g6v")
    )

    "allows jumping left or right over opponent when north wall is behind" in {
      activePlayer.pawnPosition = "c3"
      opponentPlayer.pawnPosition = "c4"

      assertValidPawnMoves("c2", "b3", "d3", "b4", "d4")
    }

    "allows jumping left or right over opponent when south wall is behind" in {
      activePlayer.pawnPosition = "d6"
      opponentPlayer.pawnPosition = "d5"

      assertValidPawnMoves("d7", "c6", "e6", "c5", "e5")
    }

    "allows jumping left or right over opponent when east wall is behind" in {
      activePlayer.pawnPosition = "f6"
      opponentPlayer.pawnPosition = "g6"

      assertValidPawnMoves("e6", "f5", "f7", "g5", "g7")
    }

    "allows jumping left or right over opponent when west wall is behind" in {
      activePlayer.pawnPosition = "i7"
      opponentPlayer.pawnPosition = "h7"

      assertValidPawnMoves("i8", "i6", "h6", "h8")
    }
  }
}