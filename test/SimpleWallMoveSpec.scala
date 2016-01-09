class SimpleWallMoveSpec extends MoveValidationSpec {
  "Simple wall move validation" should {
    "validate normal moves" in {
      assertValidWallMoves("a1v", "a8h", "h1h", "h8v")
    }

    "validates weird moves" in {
      assertInvalidWallMoves("", "e2", "test", "e4p")
    }

    "validates out of ammo" in {
      activePlayer.wallsLeft = 0
      assertInvalidWallMoves("a1v", "a8h", "h1h", "h8v")
    }
  }
}