import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameBoardTest {
  @Test
  internal fun `it should parse a string and create an internal model`() {
    // Given
    val inputStr = """
      3
      5 5
      .....
      ..x..
      ..x..
      ..x..
      .....
      """.trimIndent()

    // When
    val board = parseGameBoard(inputStr)

    // Then
    assertEquals(3, board.leftGenerationsCount())

    assertEquals(5, board.rowsCount())
    assertEquals(5, board.columnsCount())

    assertEquals(false, board.get(0, 0))
    assertEquals(true, board.get(1, 2))
    assertEquals(false, board.get(2, 1))
    assertEquals(true, board.get(2, 2))
  }

  @Test
  internal fun `it should print game board as string`() {
    // Given
    val inputStr = """
      1
      3 3
      ..x
      .x.
      .xx
    """.trimIndent()
    val board = parseGameBoard(inputStr)

    // When
    val output = printGameBoard(board)

    // Then
    assertEquals(
      """
      ..x
      .x.
      .xx
      """.trimIndent(),
      output,
    )
  }

  @Test
  internal fun `it should allow to get cells outside of a board`() {
    // Given
    val inputStr = """
      3
      5 5
      .....
      ..x..
      ..x..
      ..x..
      .....
      """.trimIndent()

    // When
    val board = parseGameBoard(inputStr)

    // Then
    assertEquals(true, board.get(1, 2))
    assertEquals(true, board.get(1, 7))

    assertEquals(false, board.get(1, 3))
    assertEquals(false, board.get(1, 8))

    assertEquals(true, board.get(1, 2))
    assertEquals(true, board.get(6, 2))

    assertEquals(false, board.get(1, 1))
    assertEquals(false, board.get(6, 1))

    assertEquals(true, board.get(-3, 2))

    assertEquals(true, board.get(1, -3))
  }

  @Test
  internal fun `it should make a step`() {
    //Given
    val inputStr = """
      3
      5 5
      .....
      ..x..
      ..x..
      ..x..
      .....
      """.trimIndent()
    val board = parseGameBoard(inputStr)

    //When
    val newBoard = board.makeStep()
    val newBoardString = printGameBoard(newBoard)

    //Then
    assertEquals("""
      .....
      .....
      .xxx.
      .....
      .....
    """.trimIndent(),
    newBoardString)
    assertEquals(2, newBoard.leftGenerationsCount())
  }

  @Test
  internal fun `it should count cell neighbours`() {
    //Given
    val inputStr = """
      3
      5 5
      xx...
      x....
      ..x..
      .....
      x....
      """.trimIndent()
    val board = parseGameBoard(inputStr)

    //When, Then
    assertEquals(0, board.countNeighbours(2, 2))
    assertEquals(3, board.countNeighbours(0, 0))
    assertEquals(2, board.countNeighbours(4, 0))
    assertEquals(4, board.countNeighbours(1, 1))
  }
}