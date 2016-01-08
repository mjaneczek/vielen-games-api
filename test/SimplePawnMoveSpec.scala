import DAOs.{GameDAO}
import interactors.{CreateMoveInteractor}
import models.{User, Move, Player, Game}

class SimplePawnMoveSpec extends InteractorSpec {
  val user = User(name = "Test", providerId = "1234")
  val secondUser = User(name = "Test 2", providerId = "1234")

  val activePlayer = Player(id = user.id, name = "Test", providerId = "1234", team = "team_1", pawnPosition = "e5", wallsLeft = 10)
  val player = Player(id = secondUser.id, name = "Test 2", providerId = "1234", team = "team_2", pawnPosition = "e9", wallsLeft = 10)

  val game = Game(players = List(activePlayer, player), activeTeam = "team_1")

  "Simple pawn move validation" should {

    "validate normal moves by one position" in {
      areValidMoves(Array("e4", "e6", "d5", "f5")) must beEqualTo(true)
    }

    "returns false for zero length move" in {
      val result = new CreateMoveInteractor(game, user, Move(moveType = "pawn", position = "e5")).call
      result must beEqualTo(false)
    }

    "not allow for out of board moves" in {
      val activePlayer = Player(id = user.id, name = "Test", providerId = "1234", team = "team_1", pawnPosition = "i5", wallsLeft = 10)
      val player = Player(id = secondUser.id, name = "Test 2", providerId = "1234", team = "team_2", pawnPosition = "e9", wallsLeft = 10)

      val game = Game(players = List(activePlayer, player), activeTeam = "team_1")

      val result = new CreateMoveInteractor(game, user, Move(moveType = "pawn", position = "j5")).call
      result must beEqualTo(false)
    }
  }

  def areValidMoves(moves : Array[String]): Boolean = {
    val horizontalPositions = Array("a","b","c","d","e","f","g","h","i")
    val verticalPositions = (1 to 9).toArray
    val possibleMoves = horizontalPositions.flatMap(horizontalPosition => verticalPositions.map(verticalPosition => horizontalPosition + verticalPosition))

    possibleMoves.forall((position) =>  (new CreateMoveInteractor(game, user, Move(moveType = "pawn", position = position)).call == (moves contains position)))
  }
}

// a9 b9 c9 d9 e9 f9 g9 h9 i9
// a8 b8 c8 d8 e8 f8 g8 h8 i8
// a7 b7 c7 d7 e7 f7 g7 h7 i7
// a6 b6 c6 d6 e6 f6 g6 h6 i6
// a5 b5 c5 d5 e5 f5 g5 h5 i5
// a4 b4 c4 d4 e4 f4 g4 h4 i4
// a3 b3 c3 d3 e3 f3 g3 h3 i3
// a2 b2 c2 d2 e2 f2 g2 h2 i2
// a1 b1 c1 d1 e1 f1 g1 h1 i1