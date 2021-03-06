class WallMoveOverlappingSpec extends MoveValidationSpec {
  "Wall move overlapping validation" should {
    setGameWalls("a1v", "c3h", "c4h", "g6v")

    "validates fully overlap moves" in {
      assertInvalidWallMoves("a1v", "c3h", "c4h", "g6v")
    }

    "validates cross moves" in {
      assertInvalidWallMoves("a1h", "c3v", "c4v", "g6h")
    }

    "validates partially overlapping" in {
      assertInvalidWallMoves("a2v", "b3h", "d3h", "b4h", "d4h", "g5v", "g7v")
    }
  }
}