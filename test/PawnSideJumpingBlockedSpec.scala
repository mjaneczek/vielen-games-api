import interactors.CreateMoveInteractor
import models.Move

class PawnSideJumpingBlockedSpec extends MoveValidationSpec {
  "Pawn jumping using borders blocked by end of board validation" should {
    "validates jumping left or right over opponent when north border is behind" in {
      activePlayer.pawnPosition = "i8"
      opponentPlayer.pawnPosition = "i9"

      assertValidPawnMoves("i7", "h8", "h9")
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "k9")).call must beFalse
    }

    "validates jumping left or right over opponent when south border is behind" in {
      activePlayer.pawnPosition = "a2"
      opponentPlayer.pawnPosition = "a1"

      assertValidPawnMoves("a3", "b2", "b1")
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "`1")).call must beFalse
    }

    "validates jumping left or right over opponent when east border is behind" in {
      activePlayer.pawnPosition = "h1"
      opponentPlayer.pawnPosition = "i1"

      assertValidPawnMoves("g1", "h2", "i2")
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "i0")).call must beFalse
    }

    "validates jumping left or right over opponent when west border is behind" in {
      activePlayer.pawnPosition = "b9"
      opponentPlayer.pawnPosition = "a9"

      assertValidPawnMoves("c9", "b8", "a8")
      new CreateMoveInteractor(game, activeUser, Move(moveType = "pawn", position = "a:")).call must beFalse
    }
  }

  "Pawn jumping using wall behind opponent blocked by another wall validation" should {
    game.moves = List(
      Move(moveType = "wall", position = "b5v"),
      Move(moveType = "wall", position = "e4v"),
      Move(moveType = "wall", position = "g6v"),
      Move(moveType = "wall", position = "f7v"),
      Move(moveType = "wall", position = "c4h"),
      Move(moveType = "wall", position = "f5h"),
      Move(moveType = "wall", position = "f6h")
    )

    "validates jumping left or right over opponent when north wall is behind" in {
      activePlayer.pawnPosition = "f4"
      opponentPlayer.pawnPosition = "f5"

      assertValidPawnMoves("f3", "g4", "g5")
    }

    "validates jumping left or right over opponent when south wall is behind" in {
      activePlayer.pawnPosition = "g8"
      opponentPlayer.pawnPosition = "g7"

      assertValidPawnMoves("g9", "h8")
    }

    "validates jumping left or right over opponent when east wall is behind" in {
      activePlayer.pawnPosition = "f6"
      opponentPlayer.pawnPosition = "g6"

      assertValidPawnMoves("e6")
    }

    "validates jumping left or right over opponent when west wall is behind" in {
      activePlayer.pawnPosition = "d5"
      opponentPlayer.pawnPosition = "c5"

      assertValidPawnMoves("e5", "d6", "c6")
    }
  }
}