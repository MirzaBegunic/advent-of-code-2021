package day04

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("day04/Day04")
    // check input
    if (input.isEmpty()) {
        throw IllegalArgumentException("Input cannot be empty")
    }
    // print results
    println(part1(input))
    println(part2(input))

    // correct results for my input data
    // 63552
    // 9020

}

fun part1(input: List<String>): Int {
    val bingo = Bingo(input)
    lateinit var boardThatHasBingo: Bingo.Board
    var bingoNumber = 0
    run numbersToDrawForEach@ {
        bingo.numbersToDraw.forEach { number ->
            // mark drawn number on all boards
            bingo.boards.forEach { board ->
                board.markDrawnNumber(number)
            }
            // check if any board has bingo
            var bingoFound = false
            run boardsForEach@ {
                bingo.boards.forEach { board ->
                    if (board.checkForBingo()) {
                        bingoFound = true
                        boardThatHasBingo = board
                        bingoNumber = number
                        return@boardsForEach
                    }
                }
            }
            if (bingoFound) {
                // bingo is found so end the search
                return@numbersToDrawForEach
            }
        }
    }
    return bingoNumber*boardThatHasBingo.sumUncheckedNumbers()
}

fun part2(input: List<String>): Int {
    val bingo = Bingo(input)
    lateinit var lastBoardThatHasBingo: Bingo.Board
    var lastBingoNumber = 0
    val setOfWinnerBoards = mutableSetOf<Bingo.Board>()
    run bingoNumbersToDrawForEach@ {
        bingo.numbersToDraw.forEach { number ->
            // mark drawn number on all boards
            bingo.boards.forEach { board ->
                board.markDrawnNumber(number)
            }
            bingo.boards.forEach { board ->
                if (board.checkForBingo()) {
                    lastBoardThatHasBingo = board
                    lastBingoNumber = number
                    setOfWinnerBoards.add(board)
                }
                if (setOfWinnerBoards.size == bingo.boards.size) {
                    // last board got bingo so end the search
                    return@bingoNumbersToDrawForEach
                }
            }
        }
    }
    return lastBingoNumber * lastBoardThatHasBingo.sumUncheckedNumbers()
}

class Bingo(
    input: List<String>
) {

    val numbersToDraw: List<Int>
    val boards: MutableList<Board>

    init {
        numbersToDraw = input[0].split(",").map { it.toInt() }
        boards = parseBoards(input)
    }

    /**
     * Create list of boards for given input
     */
    private fun parseBoards(input: List<String>): MutableList<Board> {
        val boards = mutableListOf<Board>()
        for(boardLineStartIndex in 2 until input.size step 6) {
            boards.add(parseBoard(input, boardLineStartIndex))
        }

        return boards
    }

    /**
     * Create board for given input
     */
    private fun parseBoard(input: List<String>, boardLineStartIndex: Int): Board {
        val rows = mutableListOf<List<BingoNumber>>()
        for (rowIndex in boardLineStartIndex until boardLineStartIndex+5) {
            val row = input[rowIndex].replace("  ", " ").trimStart().split(" ").map {
                BingoNumber(false, it.toInt())
            }
            rows.add(row)
        }

        val columns = mutableListOf<List<BingoNumber>>()
        for (columnIndex in 0 until rows.size) {
            val column = mutableListOf<BingoNumber>()
            rows.forEach { row ->
                column.add(row[columnIndex])
            }
            columns.add(column)
        }

        return Board(rows, columns)
    }

    inner class Board(
        private val rows: List<List<BingoNumber>>,
        private val columns: List<List<BingoNumber>>
    ) {
        /**
         * Check all numbers in board that match given number
         */
        fun markDrawnNumber(number: Int) {
            rows.forEach { row ->
                row.forEach { bingoNumber ->
                    if (bingoNumber.number == number) {
                        bingoNumber.checked = true
                    }
                }
            }
            columns.forEach { column ->
                column.forEach { bingoNumber ->
                    if (bingoNumber.number == number) {
                        bingoNumber.checked = true
                    }
                }
            }
        }

        /**
         * Checks if board has bingo
         */
        fun checkForBingo(): Boolean {
            var bingoFromRows = false
            rows.forEach { row ->
                if (checkForBingoWithGivenBingoNumbers(row)) {
                    bingoFromRows = true
                }
            }
            var bingoFromColumns = false
            columns.forEach { column ->
                if(checkForBingoWithGivenBingoNumbers(column)) {
                    bingoFromColumns = true
                }
            }

            return bingoFromRows || bingoFromColumns
        }

        /**
         * Sum all unchecked numbers in board
         */
        fun sumUncheckedNumbers(): Int {
            var sum = 0
            rows.forEach { row ->
                row.forEach { bingoNumber ->
                    if (!bingoNumber.checked) {
                        sum += bingoNumber.number
                    }
                }
            }
            return sum
        }

        /**
         * Check if collection of bingo numbers have bingo
         */
        private fun checkForBingoWithGivenBingoNumbers(bingoNumbers: Collection<BingoNumber>): Boolean {
            var bingoInCollection = true
            bingoNumbers.forEach { bingoNumber ->
                if (!bingoNumber.checked) {
                    bingoInCollection = false
                }
            }
            return bingoInCollection
        }
    }

    inner class BingoNumber(
        var checked: Boolean = false,
        val number: Int
    )
}