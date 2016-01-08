package utils

object PossiblePositions {
  def pawn = {
    val horizontalPositions = Array("a","b","c","d","e","f","g","h","i")
    val verticalPositions = (1 to 9).toArray
    horizontalPositions.flatMap(horizontalPosition => verticalPositions.map(verticalPosition => horizontalPosition + verticalPosition))
  }
}
