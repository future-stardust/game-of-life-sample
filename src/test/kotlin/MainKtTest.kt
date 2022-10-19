import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MainKtTest {
  val messages = mutableListOf<String>()
  private val outputMock: MainOutput = object : MainOutput {
    override fun printLine(msg: String) {
      messages.add(msg)
    }
  }

  private val defaultFsMock = object : FileSystem {
    override fun ifFileExist(filePath: String): Boolean {
      throw RuntimeException("ifFileExist has been called unexpectedly")
    }

    override fun readFileAsString(filePath: String): String {
      throw RuntimeException("readFileAsString has been called unexpectedly")
    }
  }

  @BeforeEach
  internal fun setUp() {
    messages.clear()
  }

  @Test
  internal fun `it should show How-to-use instructions if there is no input file param`() {
    // When
    mainHandler(emptyArray(), outputMock, defaultFsMock)

    // Then
    assertEquals(1, messages.size)
    assertEquals(Messages.noArgs, messages[0])
  }

  @Test
  internal fun `it should check if input file exists and if not then show a message`() {
    // Given
    val fsMock = object : FileSystem {
      override fun ifFileExist(filePath: String): Boolean = false
      override fun readFileAsString(filePath: String): String {
        throw RuntimeException("readFileAsString has been called unexpectedly")
      }
    }

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    assertEquals(1, messages.size)
    assertEquals(Messages.inputFileDoesNotExist, messages[0])
  }

  @Test
  internal fun `it should parse input file and throw an error if it is wrong`() {
    // Given
    val fsMock = object : FileSystem {
      override fun ifFileExist(filePath: String): Boolean = true
      override fun readFileAsString(filePath: String): String = "Wrong input file body"
    }

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    assertEquals(1, messages.size)
    assertEquals(Messages.inputFileContainsSmthWrong, messages[0])
  }

  @Test
  internal fun `it should print final board state`() {
    // Given
    val fsMock = object : FileSystem {
      override fun ifFileExist(filePath: String): Boolean = true
      override fun readFileAsString(filePath: String): String = """
          3
          5 5
          .....
          ..x..
          ..x..
          ..x..
          .....
          """.trimIndent()
    }

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    assertEquals(1, messages.size)

    val finalBoard = """
          .....
          .....
          .xxx.
          .....
          .....
          """.trimIndent()
    assertEquals(finalBoard , messages[0])
  }
}