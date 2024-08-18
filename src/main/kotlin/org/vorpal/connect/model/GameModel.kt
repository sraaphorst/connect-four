package org.vorpal.connect.model

import org.vorpal.connect.controller.Play
import org.vorpal.connect.general.*


// Row by column.
typealias Position = Pair<Int, Int>

// We change the board as moves are made.
typealias Board = List<MutableList<Player?>>

typealias ImmutableBoard = List<List<Player?>>

// Represents a line in the board.
data class Line(val player: Player, val positions: List<Position>)


class GameModel(play: Play) {
    var turn: Player = Player.PLAYER1
    val rows: Int = play.rows
    val columns: Int = play.columns
    val lineLength: Int = play.lineLength
    val board: Board = emptyBoard(rows, columns)
    val gameType: GameType = play.gameType

    init {
        // Conditions:
        // 1. The board must be able to accommodate a line of the specified length.
        // 2. The board must have height rows.
        // 3. Each row must have width columns.
        require(lineLength <= Math.max(columns, rows))
        require(board.size == rows)
        require(board.all{ it.size == columns })
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
    private fun findLastEmptyRowForColumn(col: Int): Int? =
        board.indexOfLast { row -> row[col] == null  }.takeIf { it != -1 }

    // TODO: This method is probably going to be needed for minimax to create boards that don't
    // TODO: actually mutate the data model.
    // Given a column, insert a chip, and have it descend to the last empty row if there is one since
    // gravity will pull the chip down. If there is no such empty row, an exception is raised.
    fun insertChip(player: Player, col: Int) {
        if (col !in getUnfilledColumns())
            throw IllegalArgumentException("Tried to place chip in full column $col.")
        else {
            val emptyRow = findLastEmptyRowForColumn(col)
                ?: throw IllegalStateException("Could not find empty row for column $col")
            board[col][emptyRow] = player
        }
    }

    // Performs the state transition of the current player inserting a chip into a column.
    fun playChip(col: Int) =
        insertChip(turn, col)

    // If there is a Line in the board, return it: else, null.
    fun findLine(): Line? =
        listOf(::findRow, ::findColumn, ::findDiagonal)
            .firstNotNullOfOrNull { it(board, lineLength) }

    companion object {
        // Create an empty board of the desired number of rows and columns.
        fun emptyBoard(rows: Int, columns: Int): Board =
            List(rows) { MutableList(columns) { null } }

        // Find a winning row in the given board. This is in the companion object because we want
        // to be able to also do the transpose for columns.
        private fun findRow(board: ImmutableBoard, lineLength: Int): Line? =
            board.withIndex().firstNotNullOfOrNull { (rowIdx, row) ->
                row.windowed(lineLength).withIndex().firstNotNullOfOrNull { (windowIdx, window) ->
                    window.firstOrNull()
                        ?.takeIf { player -> window.all { it == player } }
                        ?.let { player -> Line(player, (0 until lineLength).map { Position(rowIdx, windowIdx + it) }) }
                }
            }

        // For a winning column, take the transpose, find a row, and then swap it back to a column.
        private fun findColumn(board: ImmutableBoard, lineLength: Int): Line? =
            findRow(board.transpose(), lineLength)
                ?.let { Line(it.player, it.positions.map { pos -> pos.second to pos.first }) }

        // Retrieve all the diagonals on the board, regardless of contents.
        // Note that if the init code is run on the board from above, we do not need to check to see if board is a matrix.
        private fun getDiagonals(board: ImmutableBoard, lineLength: Int): List<List<Position>> {
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
    private fun findDiagonal(board: ImmutableBoard, lineLength: Int): Line? =
        getDiagonals(board, lineLength).map { diagonal ->
            val (playerX, playerY) = diagonal.first()
            board[playerX][playerY]
                ?.takeIf { diagonal.all { (x, y) -> board[x][y] == it } }
                ?.let { Line(it, diagonal) }
        }.firstOrNull()
}
