// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.vorpal.connect.controller.GameController
import org.vorpal.connect.model.BoardModel

class GameView(private val rows: SimpleIntegerProperty,
               private val columns: SimpleIntegerProperty,
               private val controller: GameController) : Pane() {

    private val canvas = Canvas(columns.value * CellSize, (rows.value + 1) * CellSize)
    private val gc = canvas.graphicsContext2D

    init {
        children.add(canvas)
        drawBoard()
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED) { handleMouseClick(it) }
    }

    // TODO: This could be optimized.
    // TODO: The only thing this method might need to do is:
    // TODO: 1. Erase arrows if a column ends up full; or
    // TODO: 2. Draw a single chip that has been added to the board.
    fun update(model: BoardModel) {
        drawBoard(model)
    }

    private fun drawBoard(model: BoardModel? = null) {
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        // Draw the arrows used to indicate chips can be dropped.
        gc.fill = ArrowColor
        model
            ?.getUnfilledColumns()
            ?.forEach { column ->
                val calc = column * CellSize + CellSize / 2
                gc.fillPolygon(
                    doubleArrayOf(calc - HalfArrowSize, calc + HalfArrowSize, calc),
                    doubleArrayOf(0.0, 0.0, ArrowSize), 3)
            }

        // Draw the chip, or the empty chip.
        gc.fill = BoardColor
        (0 until rows.value).forEach { row ->
            (0 until columns.value).forEach { column ->
                gc.fillRect(column * CellSize, (row + 1) * CellSize, CellSize, CellSize)

                // Pick the chip color.
                gc.fill = PlayerColorMapper.getColorForPlayer(model?.board?.get(row)?.get(column))
                gc.fillOval(
                    column * CellSize + ChipOffset,
                    (row + 1) * CellSize + ChipOffset,
                    ChipDiameter,
                    ChipDiameter
                )
            }
        }
    }

    // Translate mouse clicks into chip drops into columns.
    private fun handleMouseClick(evt: MouseEvent) {
        val row = (evt.y / CellSize).toInt()
        val column = (evt.x / CellSize).toInt()

        // DEBUGGING
        System.out.println("Click in col=$column, row=$row.")

        // We only care about clicks in row 0, since that is where the arrows are.
        // The controller will determine if the column is full.
        if (row == 0 && column in 0 until columns.value)
            controller.handleColumnClick(column)
    }

    companion object {
        private const val CellSize = 100.0
        private const val ArrowSize = 80.0
        private const val HalfArrowSize = ArrowSize / 2
        private const val ChipDiameter = 80.0
        private const val ChipOffset = (CellSize - ChipDiameter) / 2
        private val ArrowColor = Color.GREEN
        private val BoardColor = Color.BLUE
    }
}
