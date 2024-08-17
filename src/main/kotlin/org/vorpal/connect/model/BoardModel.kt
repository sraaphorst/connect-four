package org.vorpal.connect.model

import org.vorpal.connect.general.*


enum class Player {
    PLAYER1,
    PLAYER2;

    fun toggle(): Player = when (this) {
        PLAYER1 -> PLAYER2
        PLAYER2 -> PLAYER1
    }
}

// Row by column.
typealias Position = Pair<Int, Int>

typealias Board = List<List<Player?>>

// Represents a line in the board.
data class Line(val player: Player, val positions: List<Position>)


data class BoardModel(val turn: Player,
                      val board: Board,
                      val width: Int,
                      val height: Int,
                      val lineLength: Int) {
    init {
        // Conditions:
        // 1. The board must be able to accommodate a line of the specified length.
        // 2. The board must have height rows.
        // 3. Each row must have width columns.
        require(lineLength <= Math.max(width, height))
        require(board.size == height)
        require(board.all{ it.size == width })
    }

    // Determine if the board is empty, is not empty, is full, or is not full.
    fun isEmpty(): Boolean = board.last().isEmpty()
    fun isNotEmpty(): Boolean = !isEmpty()
    fun isFull(): Boolean = board.first().all { it != null }
    fun isNotFull(): Boolean = !isFull()

    // Return a list of all the columns that can accommodate more chips.
    fun getUnfilledColumns(): List<Int> =
        board.first().withIndex().filter { it.value == null }.map { it.index }

    // Gravity pulls the chips downward: thus, for a given column, we want the index of the last
    // empty row if one exists, and null otherwise.
    fun findLastEmptyRowForColumn(col: Int): Int? =
        board.indexOfLast { row -> row[col] == null  }.takeIf { it != -1 }

    // Given a column, insert a chip, and have it descend to the last empty row if there is one since
    // gravity will pull the chip down. If there is no such empty row, an exception is raised.
    fun insertChip(player: Player, col: Int): Board =
        if (col !in getUnfilledColumns())
            throw IllegalArgumentException("Tried to place chip in full column $col.")
        else {
            val emptyRow = findLastEmptyRowForColumn(col)
            board.mapIndexed { rIdx, row ->
                if (rIdx == emptyRow) row.mapIndexed { colIdx, v -> if (colIdx == col) player else v}
                else row
            }
        }

    // Performs the state transition of the current player inserting a chip into a column.
    fun playChip(col: Int): BoardModel =
        BoardModel(turn.toggle(), insertChip(turn, col), width, height, lineLength)

    // If there is a Line in the board, return it: else, null.
    fun findLine(): Line? =
        listOf(::findRow, ::findColumn, ::findDiagonal)
            .firstNotNullOfOrNull { it(board, lineLength) }

    companion object {
        // Find a winning row in the given board. This is in the companion object because we want
        // to be able to also do the transpose for columns.
        private fun findRow(board: Board, lineLength: Int): Line? =
            board.withIndex().firstNotNullOfOrNull { (rowIdx, row) ->
                row.windowed(lineLength).withIndex().firstNotNullOfOrNull { (windowIdx, window) ->
                    window.firstOrNull()
                        ?.takeIf { player -> window.all { it == player } }
                        ?.let { player -> Line(player, (0 until lineLength).map { Position(rowIdx, windowIdx + it) }) }
                }
            }

        // Should be the same as the above function.
//        private fun findRow(board: Board, lineLength: Int): Line? {
//            board.forEachIndexed { rowIdx, row ->
//                row.windowed(lineLength).forEachIndexed { windowIdx, window ->
//                    val player = window.firstOrNull()
//                    if (player != null && window.all { it == player }) {
//                        return Line(player, (0 until lineLength).map { Position(rowIdx, windowIdx + it) })
//                    }
//                }
//            }
//            return null
//        }

        // For a winning column, take the transpose, find a row, and then swap it back to a column.
        private fun findColumn(board: Board, lineLength: Int): Line? =
            findRow(board.transpose(), lineLength)
                ?.let { Line(it.player, it.positions.map { pos -> pos.second to pos.first }) }

        // Retrieve all the diagonals on the board, regardless of contents.
        // Note that if the init code is run on the board from above, we do not need to check to see if board is a matrix.
        private fun getDiagonals(board: Board, lineLength: Int): List<List<Position>> {
            val rows = board.size
            val cols = board.first().size

            val rightSlant = (0..(rows - lineLength)).flatMap { row ->
                (0..(cols - lineLength)).map { col ->
                    (0 until lineLength).map { row + it to col + it }
                }
            }

            val leftSlant = (0..(rows - lineLength)).flatMap { row ->
                ((lineLength - 1) until cols).map { col ->
                    (0 until lineLength).map { row + it to col - it }
                }
            }

            return rightSlant + leftSlant
        }
    }

    // Find a winning diagonal if there is one.
    private fun findDiagonal(board: Board, lineLength: Int): Line? =
        getDiagonals(board, lineLength).map { diagonal ->
            val (playerX, playerY) = diagonal.first()
            board[playerX][playerY]
                ?.takeIf { diagonal.all { (x, y) -> board[x][y] == it } }
                ?.let { Line(it, diagonal) }
        }.firstOrNull()
}
