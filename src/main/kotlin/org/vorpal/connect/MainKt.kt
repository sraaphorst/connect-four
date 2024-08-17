package org.vorpal.connect

import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.vorpal.connect.controller.SetupPanel

class MainKt : Application() {

    private lateinit var mainContainer: StackPane

    override fun start(primaryStage: Stage) {
        // Initialize properties for the values.
        val humanPlayer1 = SimpleBooleanProperty(true)
        val humanPlayer2 = SimpleBooleanProperty(false)
        val regularGame = SimpleBooleanProperty(true)
        val rowsValue = SimpleIntegerProperty(SetupPanel.ROWS_DEFAULT)
        val columnsValue = SimpleIntegerProperty(SetupPanel.COLS_DEFAULT)
        val linesValue = SimpleIntegerProperty(SetupPanel.LINE_LENGTH_DEFAULT)

        // Create the initial panel (SetupPanel in this case)
        val setupPanel = SetupPanel(humanPlayer1, humanPlayer2, regularGame, rowsValue, columnsValue, linesValue, this)

        // Main container to hold the panel
        val mainContainer = StackPane(setupPanel)
        mainContainer.padding = Insets(20.0)

        // Set up the stage
        primaryStage.title = "Connect Four Setup"
        primaryStage.scene = Scene(mainContainer, 400.0, 400.0)
        primaryStage.show()
    }

    fun play(humanPlayer1: SimpleBooleanProperty,
             humanPlayer2: SimpleBooleanProperty,
             regularGame: SimpleBooleanProperty,
             rowsValue: SimpleIntegerProperty,
             columnsValue: SimpleIntegerProperty,
             linesValue: SimpleIntegerProperty) {
        println("""Playing: h1=${humanPlayer1.get()}, h2=${humanPlayer2.get()}, regularGame=${regularGame.get()}, 
            rowsValue=${rowsValue.get()}, columnsValue=${columnsValue.get()}, linesValue=${linesValue.get()}""")
//        val gamePanel = org.vorpal.connect.controller.GamePanel(rowsValue, columnsValue, linesValue)
//        mainContainer.children.clear()
//        mainContainer.children.add(gamePanel)
    }

    fun quit() {
        (mainContainer.scene.window as? Stage)?.close()
    }
}

fun main() {
    Application.launch(MainKt::class.java)
}
