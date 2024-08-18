// By Sebastian Raaphorst, 2024.

package org.vorpal.connect

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import org.vorpal.connect.controller.*
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.Player
import org.vorpal.connect.view.GameView

class Main : Application() {
//    private lateinit var mainContainer: StackPane
    private lateinit var primaryStage: Stage

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        primaryStage.title = "Connect Four Setup"
        showSetupPanel()
    }

    private fun showSetupPanel() {
        SetupPanel.initialize { setupResult ->
            handleSetupResult(setupResult)
        }

        val scene = Scene(SetupPanel)
        primaryStage.scene = scene
        primaryStage.sizeToScene()
        primaryStage.show()
    }

    private fun handleSetupResult(setupResult: SetupResult) = when(setupResult) {
        is Play -> startGame(setupResult)
        is Quit -> primaryStage.close()
    }

    private fun startGame(play: Play) {
        // This is where the looping happens.
        println("Starting game with setup $play")

        val gameView = GameView(play.rows, play.columns)
        val gameModel = GameModel()

        // When done, we return to the setup panel.
        showSetupPanel()
    }

//    // Method to handle the transition to gameplay.
//    fun play() {
//        println(
//            """Playing: h1=${humanPlayer1.value}, h2=${humanPlayer2.value}, regularGame=${regularGame.value},
//            rowsValue=${rowsValue.value}, columnsValue=${columnsValue.value}, linesValue=${linesValue.value}"""
//        )
//        // Initialize GameView (assuming you have a GameView class)
//        val gameView = GameView(rowsValue, columnsValue)
//        val gameModel = GameModel(Player.PLAYER1, GameModel.emptyBoard(columnsValue.value, rowsValue.value), columnsValue.value, rowsValue.value, regularGame.value, linesValue.value)
//        val gameController = GameController(gameModel, gameView)
//        mainContainer.children.setAll(gameView)
//
//        // Resize the stage to fit the GamePanel
//        primaryStage.sizeToScene()
//    }
}

fun main() {
    Application.launch(Main::class.java)
}
