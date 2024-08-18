// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.Position

class GameView(private val rows: SimpleIntegerProperty,
               private val columns: SimpleIntegerProperty) : Pane() {

    private val canvas = Canvas(columns.value * CellSize, (rows.value + 1) * CellSize)
    private val gc = canvas.graphicsContext2D
    private val statusBar = Label()

    init {
        statusBar.alignment = Pos.CENTER_LEFT
        val canvasPane = StackPane(canvas)
        children.addAll(canvasPane, statusBar)
    }

    // TODO: This could be optimized.
    // TODO: The only thing this method might need to do is:
    // TODO: 1. Erase arrows if a column ends up full; or
    // TODO: 2. Draw a single chip that has been added to the board.
    fun update(model: GameModel) {
        drawBoard(model)
    }

    // Call when the game is over to erase the arrows from the screen if the game is over.
    private fun eraseArrows() {
        gc.fill = Color.BLACK
        gc.fillRect(0.0, 0.0, canvas.width, CellSize)
    }

    private fun drawBoard(model: GameModel? = null) {
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

    // The controller will register for events from this canvas,
    // maintaining the purity of the controller being the owner
    // of the model and the view.
    fun setCanvasClickListener(listener: (MouseEvent) -> Unit) {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, listener)
    }

    // If the game is over, the controller should no longer respond to events from
    // the canvas.
    fun removeCanvasClickListener(listener: (MouseEvent) -> Unit) {
        canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, listener)
    }

    // Enable clicking on the status bar: this is to reset the game when it is over.
    fun setStatusBarClickListener(listener: (MouseEvent) -> Unit) {
        statusBar.addEventHandler(MouseEvent.MOUSE_CLICKED, listener)
    }

    // Remove clicking on the status bar.
    // This should not be needed but we include it for completeness.
    fun removeStatusBarClickListener(listener: (MouseEvent) -> Unit) {
        statusBar.removeEventHandler(MouseEvent.MOUSE_CLICKED, listener)
    }

    fun setStatusBarText(text: String) {
        statusBar.text = text
    }

    // Translate a mouse (x, y) position to a (row, column) position on the board.
    // Note that the arrow row at the top is considered to be row -1 since
    // it does not contribute to the actual game model.
    fun mousePosToBoardPosition(event: MouseEvent): Position =
        Position((event.x / CellSize).toInt() - 1, (event.y / CellSize).toInt())


    companion object {
        const val CellSize = 100.0
        private const val ArrowSize = 80.0
        private const val HalfArrowSize = ArrowSize / 2
        private const val ChipDiameter = 80.0
        private const val ChipOffset = (CellSize - ChipDiameter) / 2
        private val ArrowColor = Color.GREEN
        private val BoardColor = Color.BLUE
    }
}
