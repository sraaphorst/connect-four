// By Sebastian Raaphorst, 2024.

package org.vorpal.connect

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import org.vorpal.connect.controller.*
import org.vorpal.connect.model.GameModel
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
        println("Starting game with setup $play")

        val gameView = GameView(play.rows, play.columns)
        val gameModel = GameModel(play)
        GameController(gameModel, gameView) { showSetupPanel() }

        primaryStage.scene = Scene(gameView)
        primaryStage.sizeToScene()
        primaryStage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}
