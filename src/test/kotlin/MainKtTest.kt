import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MainKtTest {
  private val outputMock: MainOutput = mockk()
  private val defaultFsMock: FileSystem = mockk()

  @BeforeEach
  internal fun setUp() {
    clearAllMocks()
    every { outputMock.printLine(any()) } just Runs
  }

  @Test
  internal fun `it should show How-to-use instructions if there is no input file param`() {
    // When
    mainHandler(emptyArray(), outputMock, defaultFsMock)

    // Then
    verify(exactly = 1) { outputMock.printLine(Messages.noArgs) }
  }

  @Test
  internal fun `it should check if input file exists and if not then show a message`() {
    // Given
    val fsMock: FileSystem = mockk()
    every { fsMock.ifFileExist(any()) } returns false

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    verify(exactly = 1) { outputMock.printLine(Messages.inputFileDoesNotExist) }
  }

  @Test
  internal fun `it should parse input file and throw an error if it is wrong`() {
    // Given
    val fsMock: FileSystem = mockk()
    every { fsMock.ifFileExist(any()) } returns true
    every { fsMock.readFileAsString(any()) } returns "Wrong input file body"

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    verify(exactly = 1) { outputMock.printLine(Messages.inputFileContainsSmthWrong) }
  }

  @Test
  internal fun `it should print final board state`() {
    // Given
    val fsMock: FileSystem = mockk()
    every { fsMock.ifFileExist(any()) } returns true
    every { fsMock.readFileAsString(any()) } returns """
          3
          5 5
          .....
          ..x..
          ..x..
          ..x..
          .....
          """.trimIndent()

    // When
    mainHandler(arrayOf("input.txt"), outputMock, fsMock)

    // Then
    val finalBoard = """
          .....
          .....
          .xxx.
          .....
          .....
          """.trimIndent()
    verify(exactly = 1) { outputMock.printLine(finalBoard) }
  }

  @Test
  internal fun `it should print each generation if '-printEachGeneration' param is passed`() {
    // Given
    val fsMock: FileSystem = mockk()
    every { fsMock.ifFileExist(any()) } returns true
    every { fsMock.readFileAsString(any()) } returns """
          1
          5 5
          .....
          ..x..
          ..x..
          ..x..
          .....
          """.trimIndent()

    // When
    mainHandler(arrayOf("input.txt", "-printEachGeneration"), outputMock, fsMock)

    // Then
    verify {
      outputMock.printLine("""
          GENERATION 0
          .....
          ..x..
          ..x..
          ..x..
          .....
          
          """.trimIndent()
      )
      outputMock.printLine("""
          GENERATION 1
          .....
          .....
          .xxx.
          .....
          .....
          
          """.trimIndent()
      )
    }
  }
}
