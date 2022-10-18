fun playGame(board: GameBoard): GameBoard {
  var brd = board
  for (i in 0 until board.leftGenerationsCount()) {
    brd = brd.makeStep()
  }
  return brd
}
