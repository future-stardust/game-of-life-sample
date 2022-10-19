import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GamePlayerTest {
  @Test
  internal fun `it should run all generations and print the final board state`() {
    // Given
    val initialBoard = parseGameBoard("""
          3
          5 5
          .....
          ..x..
          ..x..
          ..x..
          .....
          """.trimIndent()
    )

    // When
    val finalBoard = playGame(initialBoard)

    // Then
    val expectedFinalBoard = parseGameBoard("""
      0
      5 5
      .....
      .....
      .xxx.
      .....
      .....
      """.trimIndent())
    assertEquals(expectedFinalBoard, finalBoard)
  }
}
