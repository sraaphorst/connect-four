// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.controller

import javafx.scene.control.Alert
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.GameType
import org.vorpal.connect.model.Line
import org.vorpal.connect.model.Player
import org.vorpal.connect.view.GameView

class GameController(
    private val model: GameModel,
    private val view: GameView,
    private val onGameEnd: () -> Unit) {

    init {
        // Set up the controller to listen to the chip sliding arrows.
        view.setArrowClickListener { column -> handleArrowClick(column) }
        view.update(model)
        statusTurn()
    }

    // Display player's turn.
    private fun statusTurn() {
        view.setStatusBarText("${PlayerString[model.turn]}'s turn.")
    }

    // Handle winning of current player and change the controls.
    // Turn the status bar into a reset.
    // Create a border around the line.
    private fun handleWin(line: Line) {
        view.emphasizeLine(line)

        // Display a dialog depending on if regular or misere is being played.
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Game Over"
        alert.headerText = null
        val winner = when (model.gameType) {
            GameType.REGULAR -> model.turn
            GameType.MISERE -> model.turn.toggle()
        }
        val winnerText = "${PlayerString[winner]} wins!"
        view.setStatusBarText(winnerText)
        alert.contentText = "$winnerText\nClick Okay to return to setup."
        alert.showAndWait()
        onGameEnd()
    }

    // Handle a tie.
    private fun handleTie() {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Game Over"
        val winnerText = "Game has been tied."
        alert.contentText = "$winnerText\nClick Okay to return to setup."
        alert.showAndWait()
        onGameEnd()
    }

    // The canvas was clicked: determine if a chip is inserted and there is a winner.
    private fun handleArrowClick(column: Int) {
        // The only valid positions to click in are the arrow row and in the
        // columns that are not yet full. Ignore other events.
        println("Clicked column: $column")
        println("Unfilled columns: ${model.getUnfilledColumns()}")
        (0 until model.columns).forEach { column ->
            println("Find last empty row for column: ${model.findLastEmptyRowForColumn(column)}")
        }

        if (column in model.getUnfilledColumns()) {
            // Add a chip to the column, check if there is a line, and if so, report the result.
            println("Playing chip...")
            model.playChip(column)
            view.update(model)
            println("Played chip...")

            // Check if this introduces a line.
            val line = model.findLine()
            if (line != null)
                handleWin(line)
            else if (model.isFull())
                handleTie()
            else
                nextRound()
        } else
            println("Click at $column ignored.")
    }

    private fun nextRound() {
        model.turn = model.turn.toggle()
        statusTurn()
    }

    companion object {
        private val PlayerString = mapOf(Player.PLAYER1 to "Player 1", Player.PLAYER2 to "Player 2")
    }
}