interface GameBoard {
  fun rowsCount(): Int
  fun columnsCount(): Int
  fun leftGenerationCount(): Int
  fun get(row: Int, column: Int): Boolean
}

private data class Point(val row: Int, val column: Int)

private data class GameBoardImpl(
  private val points: Set<Point>,
  private val rows: Int,
  private val columns: Int,
  private val generations: Int,
) : GameBoard {
  override fun get(row: Int, column: Int): Boolean = points.contains(Point((row + rows) % rows, (column + columns) % columns))
  override fun rowsCount(): Int = rows
  override fun columnsCount(): Int = columns
  override fun leftGenerationCount(): Int = generations
}

fun parseGameBoard(inputStr: String): GameBoard {
  val lines = inputStr.lines()
  val generations = lines[0].toInt()
  val rows = lines[1].split(' ')[0].toInt()
  val columns = lines[1].split(' ')[1].toInt()

  val points = mutableSetOf<Point>()
  for (row in 0 until rows) {
    for (column in 0 until columns) {
      if (lines[row + 2][column] == 'x') {
        points += Point(row, column)
      }
    }
  }

  return GameBoardImpl(points, rows, columns, generations)
}

fun printGameBoard(board: GameBoard): String {
  var str = ""
  for (row in 0 until board.rowsCount()) {
    for (column in 0 until board.columnsCount()) {
      str += if (board.get(row, column)) {
        "x"
      } else {
        "."
      }
    }
    if (row != board.rowsCount() - 1) str += '\n'
  }

  return str
}
