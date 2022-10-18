interface GameBoard {
  /** Board height */
  fun rowsCount(): Int

  /** Board width */
  fun columnsCount(): Int

  /** How many generations should pass until the game is ended */
  fun leftGenerationsCount(): Int

  /**
   * Get a board cell with coordinates X = row, Y = column.
   * A point with coordinates (0, 0) is located in the top left corner. The X axis is directed from left to right.
   * The Y axis is directed from top to bottom.
   */
  fun get(row: Int, column: Int): Boolean

  /** Generate a new next generation board. */
  fun makeStep(): GameBoard

  /** Count alive neighbours of a point with coordinates (row, column). */
  fun countNeighbours(row: Int, column: Int): Int
}

/**
 * Parse a string into a game board.
 *
 * Example of an input string:
 * ```
 * 3
 * 5 5
 * .....
 * ..x..
 * ..x..
 * ..x..
 * .....
 * ```
 */
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

/** Convert a game board into a multiline string. */
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
  override fun leftGenerationsCount(): Int = generations

  override fun makeStep(): GameBoard {
    val newPoints: MutableSet<Point> = mutableSetOf()

    for (row in 0 until rows) {
      for (column in 0 until columns) {
        when (countNeighbours(row, column)) {
          3 -> newPoints += Point(row, column)
          2-> if (get(row, column)) newPoints += Point(row, column)
        }
      }
    }

    return copy(points = newPoints, generations = generations - 1)
  }

  override fun countNeighbours(row: Int, column: Int): Int {
    var count = 0

    count += if (get(row - 1, column - 1)) 1 else 0
    count += if (get(row - 1, column)) 1 else 0
    count += if (get(row - 1, column + 1)) 1 else 0

    count += if (get(row, column - 1)) 1 else 0
    count += if (get(row, column + 1)) 1 else 0

    count += if (get(row + 1, column - 1)) 1 else 0
    count += if (get(row + 1, column)) 1 else 0
    count += if (get(row + 1, column + 1)) 1 else 0

    return count
  }
}

fun playGame(board: GameBoard): GameBoard {
  var brd = board
  for (i in 0 until board.leftGenerationsCount()) {
    brd = brd.makeStep()
  }
  return brd
}
