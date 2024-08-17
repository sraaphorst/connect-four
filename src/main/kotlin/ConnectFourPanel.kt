//import org.vorpal.Position
//import org.vorpal.connect.model.Board
//import org.vorpal.connect.model.Line
//import org.vorpal.connect.model.Player
//import org.vorpal.transpose
//import java.awt.Canvas
//import java.awt.Color
//import java.awt.Dimension
//import java.awt.event.MouseAdapter
//import java.awt.event.MouseEvent
//
//
//
//
//
//data class BoardState(val turn: Player,
//                      val board: Board,
//                      val width: Int,
//                      val height: Int,
//                      val lineLength: Int) {
//
//
//    // If there is a Line in the board, return it: else, null.
//    fun findLine(): Line? =
//        listOf(Companion::findRow, Companion::findColumn, ::findDiagonal)
//            .firstNotNullOfOrNull { it(board, lineLength) }
//
//    companion object {
//        private fun findRow(board: Board, lineLength: Int): Line? =
//            board.withIndex().firstNotNullOfOrNull { (rowIdx, row) ->
//                row.windowed(lineLength).withIndex().firstNotNullOfOrNull { (windowIdx, window) ->
//                    window.firstOrNull()?.takeIf { player -> window.all { it == player } }
//                        ?.let { player -> Line(player, (0 until lineLength).map { Position(rowIdx, windowIdx + it) }) }
//                }
//            }
//        // Find a winning row in the given board. This is in the companion object because we want
//        // to be able to also do the transpose for columns.
//        private fun findRow2(board: Board, lineLength: Int): Line? {
//            board.forEachIndexed { rowIdx, row ->
//                row.windowed(lineLength).forEachIndexed { windowIdx, window ->
//                    val player = window.firstOrNull()
//                    if (player != null && window.all { it == player }) {
//                        return Line(player, (0 until lineLength).map { Pair(rowIdx, windowIdx + it) })
//                    }
//                }
//            }
//            return null
//        }
//
//        // For a winning column, take the transpose, find a row, and then swap it back to a column.
//        private fun findColumn(board: Board, lineLength: Int): Line? =
//            findRow(board.transpose(), lineLength)
//                ?.let { Line(it.player, it.positions.map { pos -> pos.second to pos.first }) }
//
//        // Retrieve all the diagonals on the board, regardless of contents.
//        private fun getDiagonals(board: Board, lineLength: Int): List<List<Position>> {
//            val rowCount = board.size
//            val colCount = board.first().size
//            if (board.all { it.size != colCount })
//                throw IllegalArgumentException("Board is not a matrix.")
//
//            val rightSlant = (0..(rowCount - lineLength)).flatMap { row ->
//                (0..(colCount - lineLength)).map { col ->
//                    (0 until lineLength).map { row + it to col + it }
//                }
//            }
//
//            val leftSlant = (0..(rowCount - lineLength)).flatMap { row ->
//                ((lineLength - 1) until colCount).map { col ->
//                    (0 until lineLength).map { row + it to col - it }
//                }
//            }
//
//            return rightSlant + leftSlant
//        }
//    }
//
//    // Find a winning diagonal if there is one.
//    private fun findDiagonal(board: Board, lineLength: Int): Line? =
//        getDiagonals(board, lineLength).map { diagonal ->
//            val (playerX, playerY) = diagonal.first()
//            val player = board[playerX][playerY]
//            player?.takeIf { diagonal.all { (x, y) -> board[x][y] == it } }
//                ?.let { Line(it, diagonal) }
//        }.firstOrNull()
//}
//
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