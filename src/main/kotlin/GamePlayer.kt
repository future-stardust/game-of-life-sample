fun playGame(board: GameBoard): GameBoard {
  var brd = board
  for (i in 0 until board.leftGenerationsCount()) {
    brd = brd.makeStep()
  }
  return brd
}

fun playGameAndReturnAllGenerations(initialBoard: GameBoard): List<GameBoard> {
  val generations = mutableListOf(initialBoard)
  for (i in 0 until initialBoard.leftGenerationsCount()) {
    generations.add(generations.last().makeStep())
  }
  return generations
}