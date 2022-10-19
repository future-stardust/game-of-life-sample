import java.io.File

fun main(args: Array<String>) {
  val output = object : MainOutput {
    override fun printLine(msg: String) {
      println(msg)
    }
  }

  val fs = object : FileSystem {
    override fun ifFileExist(filePath: String): Boolean = File(filePath).exists()
    override fun readFileAsString(filePath: String): String = File(filePath).readText()
  }

  // This is an example for DI (dependency injection) - we pass output (abstracts a console) and fs (abstracts a file
  // system) into a main controller
  mainHandler(args, output, fs)
}

interface MainOutput {
  fun printLine(msg: String)
}

interface FileSystem {
  fun ifFileExist(filePath: String): Boolean
  fun readFileAsString(filePath: String): String
}

fun mainHandler(args: Array<String>, output: MainOutput, fs: FileSystem) {
  if (args.isEmpty()) {
    output.printLine(Messages.noArgs)
    return
  }

  val inputFilePath = args[0]
  if (!fs.ifFileExist(inputFilePath)) {
    output.printLine(Messages.inputFileDoesNotExist)
    return
  }

  val board = try {
    parseGameBoard(fs.readFileAsString(inputFilePath))
  } catch (e: Exception) {
    null
  }

  if (board == null) {
    output.printLine(Messages.inputFileContainsSmthWrong)
  } else {
    val finalBoard = playGame(board)
    output.printLine(printGameBoard(finalBoard))
  }
}

object Messages {
  val noArgs = """
    Hello! It's Game of Life. Please, pass me an input file. Example:
    > gameoflife input.txt
  """.trimIndent()

  val inputFileDoesNotExist = """
    Input file doesn't exist
  """.trimIndent()

  val inputFileContainsSmthWrong = """
    inputFileContainsSmthWrong
  """.trimIndent()
}