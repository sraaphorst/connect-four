// By Sebastian Raaphorst, 2024.

package org.vorpal.connect

import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.vorpal.connect.controller.GameController
import org.vorpal.connect.controller.SetupPanel
import org.vorpal.connect.model.GameModel
import org.vorpal.connect.model.Player
import org.vorpal.connect.view.GameView

class Main : Application() {

    private lateinit var mainContainer: StackPane
    private lateinit var primaryStage: Stage

    // State properties for the application
    private val humanPlayer1 = SimpleBooleanProperty(true)
    private val humanPlayer2 = SimpleBooleanProperty(false)
    private val regularGame = SimpleBooleanProperty(true)
    private val rowsValue = SimpleIntegerProperty(SetupPanel.ROWS_DEFAULT)
    private val columnsValue = SimpleIntegerProperty(SetupPanel.COLS_DEFAULT)
    private val linesValue = SimpleIntegerProperty(SetupPanel.LINE_LENGTH_DEFAULT)

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage  // Store reference to primaryStage

        // Initialize the main container
        mainContainer = StackPane()
        mainContainer.padding = Insets(20.0)

        // Set up the stage
        primaryStage.title = "Connect Four"
        primaryStage.scene = Scene(mainContainer)
        primaryStage.show()

        // Display the SetupPanel initially.
        setup()
    }

    // Method to display the SetupPanel.
    fun setup() {
        val setupPanel = SetupPanel(
            humanPlayer1, humanPlayer2, regularGame,
            rowsValue, columnsValue, linesValue, this
        )
        mainContainer.children.setAll(setupPanel)

        // Resize the stage to fit the SetupPanel
        primaryStage.sizeToScene()
    }

    // Method to handle the transition to gameplay.
    fun play() {
        println(
            """Playing: h1=${humanPlayer1.value}, h2=${humanPlayer2.value}, regularGame=${regularGame.value}, 
            rowsValue=${rowsValue.value}, columnsValue=${columnsValue.value}, linesValue=${linesValue.value}"""
        )
        // Initialize GameView (assuming you have a GameView class)
        val gameView = GameView(rowsValue, columnsValue)
        val gameModel = GameModel(Player.PLAYER1, GameModel.emptyBoard(columnsValue.value, rowsValue.value), columnsValue.value, rowsValue.value, regularGame.value, linesValue.value)
        val gameController = GameController(gameModel, gameView)
        mainContainer.children.setAll(gameView)

        // Resize the stage to fit the GamePanel
        primaryStage.sizeToScene()
    }

    // Method to quit the application
    fun quit() {
        primaryStage.close()
    }
}

fun main() {
    Application.launch(Main::class.java)
}
