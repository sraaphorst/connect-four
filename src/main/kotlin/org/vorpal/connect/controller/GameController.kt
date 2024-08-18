// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.controller

import javafx.scene.input.MouseEvent
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.Player
import org.vorpal.connect.view.GameView

class GameController(
    private val model: GameModel,
    private val view: GameView) {

    init {
        view.setCanvasClickListener { event -> handleCanvasClickEvent(event) }
        view.setStatusBarText("Ready")
    }

    // Display player's turn.
    private fun statusTurn() {
        view.setStatusBarText("${PlayerString[model.turn]}'s turn.'}")
    }

    // Handle winning of current player and change the controls.
    // Turn the status bar into a reset.
    // Create a border around the line.
    private fun winnerAction() {
        val line = model.findLine()
        if (line != null) {
            // The line should have been achieved on the previous round, amd thus mode determines winner.
            val winner = if (model.normalGame) model.turn.toggle() else model.turn
            view.setStatusBarText("${PlayerString[winner]} wins! Click to return to setup.")
            view.setStatusBarClickListener { Main }
        }
        view.setStatusBarText("${PlayerString[model.turn] }")
    }


    fun handleCanvasClickEvent(event: MouseEvent) {
        val (row, column) = view.mousePosToBoardPosition(event)

        // The only valid positions to click in are the arrow row and in the
        // columns that are not yet full. Ignore other events.
        if (row == -1 && column in model.getUnfilledColumns()) {
            // Add a chip to the column, check if there is a line, and if so, report the result.
            val player = model.turn
            val newBoard = model.playChip(column)

        } else
            print("Click at (${event.x}, ${event.y}) -> row $row col $column ignored.")

        model.playChip(col)
        view.update(model)
    }

    companion object {
        private val PlayerString = mapOf(Player.PLAYER1 to "Player 1", Player.PLAYER2 to "Player 2")
    }
}