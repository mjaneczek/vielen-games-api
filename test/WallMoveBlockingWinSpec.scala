class WallMoveBlockingWinSpec extends MoveValidationSpec {
  "Wall move blocking win validation" should {
    "cannot completely box pawn" in {
      activePlayer.pawnPosition = "e5"
      setGameWalls("e5h", "d4v", "f4v")

      assertValidWallMoves("e2h")
      assertInvalidWallMoves("e4h", "e3h")
    }

    "cannot hide pawn in snail house" in {
      activePlayer.pawnPosition = "e5"
      opponentPlayer.pawnPosition = "e8"

      setGameWalls(
        "e3h",
        "f4v", "f6v",
        "e7h", "c7h",
        "b6v", "b4v", "b2v",
        "c1h", "e1h", "g1h",
        "h2v", "h4v", "h6v", "h8v",
        "g8h", "e8h", "c8h", "a8h"
      )

      assertValidWallMoves("d3v", "d4v", "d5v", "d6v")
      assertInvalidWallMoves("c3h", "d2v", "e2v", "f2v", "g3h", "g4h", "g5h", "g6h", "g7h", "f8v", "a7h", "a1h", "d7v",
                             "d8v", "b8v", "d1v", "f1v", "h3h", "h5h", "h7h")
    }

    "can separate players on good sides" in {
      activePlayer.pawnPosition = "e6"
      opponentPlayer.pawnPosition = "e5"
      setGameWalls("a5h", "c5h", "e5h", "g5h", "h5v")

      assertValidWallMoves("h6h", "h4h")
    }

    "cannot separate players on wrong sides" in {
      activePlayer.pawnPosition = "e5"
      opponentPlayer.pawnPosition = "e6"
      setGameWalls("a5h", "c5h", "e5h", "g5h", "h5v")

      assertInvalidWallMoves("h6h", "h4h")
    }
  }
}