package org.vorpal.connect.view

import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.Line

class GameView(private val rows: Int, private val columns: Int) : VBox() {

    private val arrowPane = HBox()
    private val canvas = Canvas(columns * CellSize, rows * CellSize)
    private val statusBar = Label()
    private val gc = canvas.graphicsContext2D

    init {
        // Set up the status bar at the bottom
        statusBar.alignment = Pos.CENTER_LEFT
        statusBar.prefHeight = 30.0

        // Set up the row of arrows above the board
        arrowPane.prefHeight = CellSize
        arrowPane.alignment = Pos.CENTER
        arrowPane.children.addAll(List(columns) { column ->
            StackPane().apply {
                prefWidth = CellSize
                prefHeight = CellSize
                val arrow = createArrow(column)
                children.add(arrow)
            }
        })

        // Add the arrowPane, canvasPane, and statusBar to the VBox
        val canvasPane = StackPane(canvas)
        canvasPane.alignment = Pos.CENTER
        children.addAll(arrowPane, canvasPane, statusBar)
    }

    private fun createArrow(column: Int): Canvas {
        val arrowCanvas = Canvas(CellSize, CellSize)
        val gc = arrowCanvas.graphicsContext2D
        val calc = CellSize / 2
        gc.fill = ArrowColor
        gc.fillPolygon(
            doubleArrayOf(calc - HalfArrowSize, calc + HalfArrowSize, calc),
            doubleArrayOf(CellSize / 4, CellSize / 4, CellSize / 2 + HalfArrowSize),
            3
        )
        return arrowCanvas
    }

    // Method to register a listener for arrow clicks
    fun setArrowClickListener(listener: (Int) -> Unit) {
        arrowPane.children.forEachIndexed { index, node ->
            node.setOnMouseClicked {
                listener(index)
            }
        }
    }

    fun update(model: GameModel) {
        drawBoard(model)
    }

    private fun drawBoard(model: GameModel? = null) {
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        // Draw the chip, or the empty chip.
        (0 until rows).forEach { row ->
            (0 until columns).forEach { column ->
                gc.fill = BoardColor
                gc.fillRect(column * CellSize, row * CellSize, CellSize, CellSize)

                // Pick the chip color.
                gc.fill = PlayerColorMapper.getColorForPlayer(model?.board?.get(row)?.get(column))
                gc.fillOval(
                    column * CellSize + ChipOffset,
                    row * CellSize + ChipOffset,
                    ChipDiameter,
                    ChipDiameter
                )
            }
        }
    }

    fun emphasizeLine(line: Line) {
        val oldStrokeColor = gc.stroke
        val oldLineWidth = gc.lineWidth

        gc.stroke = Color.CYAN
        gc.lineWidth = EmphasizedWidth
        line.positions.map { (row, column) ->
            gc.strokeOval(
                column * CellSize + ChipOffset,
                row * CellSize + ChipOffset,
                ChipDiameter,
                ChipDiameter
            )
        }

        gc.lineWidth = oldLineWidth
        gc.stroke = oldStrokeColor
    }

    fun setStatusBarText(text: String) {
        statusBar.text = text
    }

    companion object {
        const val CellSize = 100.0
        private const val ArrowSize = 80.0
        private const val HalfArrowSize = ArrowSize / 2
        private const val ChipDiameter = 80.0
        private const val ChipOffset = (CellSize - ChipDiameter) / 2
        private const val EmphasizedWidth = 5.0
        private val ArrowColor = Color.GREEN
        private val BoardColor = Color.BLUE
    }
}
