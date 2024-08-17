//package org.vorpal
//
//import java.awt.Canvas
//import java.awt.Color
//import java.awt.Dimension
//import java.awt.event.MouseAdapter
//import java.awt.event.MouseEvent
//
//enum class Player {
//    PLAYER1,
//    PLAYER2,
//}
//
//// Represent the board, which has tiles or empty spaces (null).
//typealias Board = List<List<Position?>>
//
//// Represents a line in the board.
//typealias Line = List<Position>
//
//// Find a winning row in the board.
//fun findWinningRow(board: Board, winLength: Int): Line? {
//    board.forEachIndexed { rowIdx, row ->
//        row.windowed(winLength).forEachIndexed { windowIdx, window ->
//            if (window.all { it == window.first() && it != null }) {
//                return (0 until winLength).map { Pair(rowIdx, windowIdx + it) }
//            }
//        }
//    }
//    return null
//}
//
//// For a winning column, take the transpose, find a row, and then transpose it back to a column.
//fun findWinningColumn(board: Board, winLength: Int): Line? =
//    findWinningRow(board.transpose(), winLength)?.transpose()
//
//fun getDiagonals(board: Board, winLength: Int): List<List<Position>> {
//    val rowCount = board.size
//    val colCount = board.first().size
//    if (board.all { it.size != colCount })
//        throw IllegalArgumentException("Board is not a matrix.")
//
//    val rightSlant = (0..(rowCount - winLength)).flatMap { row ->
//        (0..(colCount - winLength)).map { col ->
//            (0 until winLength).map { row + it to col + it}
//        }
//    }
//
//    val leftSlant = (0..(rowCount - winLength)).flatMap { row ->
//        ((winLength - 1 ) until colCount).map { col ->
//            (0 until winLength).map { row + it to col - it}
//        }
//    }
//
//    return rightSlant + leftSlant
//}
//
//fun findWinnningDiagonal(board: Board, winLength: Int): Line? =
//    getDiagonals(board, winLength).forEach { diagonal ->
//
//
//    }
//
///**
// * This class represents the state of the board:
// * 1. The player whose turn it is.
// * 2. The height of the board in rows.
// * 3. The width of the board in rows.
// * 4. The occupied tiles, which determine if someone has won / lost.
// */
//data class ConnectFourState(val player: Player,
//                            val width: Int = ConnectFourState.width,
//                            val height: Int = ConnectFourState.height,
//                            val lineLength: Int = ConnectFourState.lineLength,
//                            val board: Board = List(height) { List(width) { null } }) {
//    // Check for winning conditions.
//    fun findWinningRow(): Line? {
//        board.forEachIndexed { rowIdx, row ->
//            row.windowed(lineLength).forEachIndexed { windowIdx, window ->
//                if (window.all { it == window.first() && it != null }) {
//                    return Line(window.first()!!, (0 until lineLength).map { Pair(rowIdx, windowIdx + it) }
//                    )
//                }
//            }
//        }
//        return null
//    }
//
//    fun findWinningColumn(): Line? {
//        val line = findWinningRow(board.transpose())
//    }
//
//    // Check for a winner / loser, depending on whether playing misère version.
//    fun findRow(pieces: List<List<Player>> = tiles): ConnectFourLine? {
//        // First check for rows (or if the transpose, columns).
//        tailrec fun aux(rowIdx: Int, colIdx: Int): ConnectFourLine? {
//            // If we are out of rows, then there was no row.
//            if (rowIdx >= height) return null
//
//            // If we don't have enough room left, go to the next row if there is one.
//            if (colIdx <= width - lineLength) {
//                val segment = pieces[rowIdx].subList(colIdx, colIdx + lineLength)
//                val playerLine = when {
//                    segment.all { it == Player.HUMAN } -> Player.HUMAN
//                    segment.all { it == Player.COMPUTER } -> Player.COMPUTER
//                    else -> null
//                }
//
//                // Line in rowIdx from colIdx spanning lineLength tiles.
//                if (playerLine != null)
//                    return ConnectFourLine(playerLine, (colIdx until (colIdx + lineLength)).map { rowIdx to it })
//            }
//
//            // Advance to the next row.
//            return aux(rowIdx + 1, 0)
//        }
//
//        return null
//    }
//
//    fun findCol(): ConnectFourLine? =
//        findRow(tiles.transpose())?.let { ConnectFourLine(it.player, it.tiles.transpose()) }
//
//
//    fun findRightDiag22
//
//
//    companion object {
//        const val width = 7
//        const val height = 6
//        const val lineLength = 4
//    }
//}
//
//class ConnectFourPanel(val player: Player, val board: ConnectFourBoard): Canvas() {
//    init {
//        background = Color.BLUE
//        preferredSize = Dimension(board.width * tileWidth, board.height * tileHeight)
//
//        addMouseListener(object: MouseAdapter() {
//            override fun mouseClicked(e: MouseEvent) {
//                // Only the column matters: gravity handles the rest.
//                val col = e.x / (board.width / tileWidth)
//                if (player == Player.HUMAN) {
//
//                }
//
//            }
//        })
//    }
//
//    companion object {
//        const val tileWidth = 100
//        const val tileHeight = 100
//    }
//}