// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.controller

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import org.vorpal.connect.model.GameType


// We either play or quit.
sealed class SetupResult

data class Play(val rows: Int,
                val columns: Int,
                val lineLength: Int,
                val isPlayer1Human: Boolean,
                val isPlayer2Human: Boolean,
                val gameType: GameType
): SetupResult()

data object Quit: SetupResult()

class SetupPanel(
    private val humanPlayer1: SimpleBooleanProperty,
    private val humanPlayer2: SimpleBooleanProperty,
    private val regularGame: SimpleBooleanProperty,
    private val rowsValue: SimpleIntegerProperty,
    private val columnsValue: SimpleIntegerProperty,
    private val linesValue: SimpleIntegerProperty,
    private val onSetupComplete: (SetupResult) -> Unit
) : GridPane() {

    init {
        // Setting up the GridPane layout.
        prefWidth = 400.0
        hgap = 20.0
        vgap = 20.0
        padding = Insets(20.0)

        // Adjust Column Constraints
        columnConstraints.addAll(
            ColumnConstraints(120.0), // Label column, slightly wider to fit longer text
            ColumnConstraints(180.0), // Slider column, slightly narrower
            ColumnConstraints(30.0)   // TextField column, just wide enough for two characters
        )

        // Player 1 Toggle
        val player1Label = Label("Player 1:")
        val player1ToggleGroup = ToggleGroup()
        val player1HumanRadio = RadioButton("Human")
        val player1ComputerRadio = RadioButton("Computer")
        player1HumanRadio.toggleGroup = player1ToggleGroup
        player1ComputerRadio.toggleGroup = player1ToggleGroup
        player1HumanRadio.isSelected = true
        val player1ToggleBox = HBox(10.0, player1HumanRadio, player1ComputerRadio)
        player1ToggleBox.alignment = Pos.TOP_LEFT
        add(player1Label, 0, 0)
        add(player1ToggleBox, 1, 0, 2, 1)
        humanPlayer1.bind(player1HumanRadio.selectedProperty())

        // Player 2 Toggle
        val player2Label = Label("Player 2:")
        val player2ToggleGroup = ToggleGroup()
        val player2HumanRadio = RadioButton("Human")
        val player2ComputerRadio = RadioButton("Computer")
        player2HumanRadio.toggleGroup = player2ToggleGroup
        player2ComputerRadio.toggleGroup = player2ToggleGroup
        player2ComputerRadio.isSelected = true
        val player2ToggleBox = HBox(10.0, player2HumanRadio, player2ComputerRadio)
        player2ToggleBox.alignment = Pos.TOP_LEFT
        add(player2Label, 0, 1)
        add(player2ToggleBox, 1, 1, 2, 1)
        humanPlayer2.bind(player2HumanRadio.selectedProperty())

        // Gameplay Toggle
        val gameplayLabel = Label("Gameplay:")
        val gameplayToggleGroup = ToggleGroup()
        val gameplayRegularRadio = RadioButton("Regular")
        val gameplayMisereRadio = RadioButton("Misere")
        gameplayRegularRadio.toggleGroup = gameplayToggleGroup
        gameplayMisereRadio.toggleGroup = gameplayToggleGroup
        gameplayRegularRadio.isSelected = true
        val gameplayToggleBox = HBox(10.0, gameplayRegularRadio, gameplayMisereRadio)
        gameplayToggleBox.alignment = Pos.TOP_LEFT
        add(gameplayLabel, 0, 2)
        add(gameplayToggleBox, 1, 2, 2, 1)
        regularGame.bind(gameplayRegularRadio.selectedProperty())

        // Rows Slider
        val rowsLabel = Label("Rows:")
        val rowsSlider = Slider(4.0, 10.0, 6.0)
        rowsSlider.majorTickUnit = 1.0
        rowsSlider.minorTickCount = 0
        rowsSlider.isSnapToTicks = true
        rowsSlider.isShowTickMarks = true
        rowsSlider.isShowTickLabels = true
        rowsValue.bind(rowsSlider.valueProperty().asObject().map(Double::toInt))
        val rowsTextField = TextField()
        rowsTextField.textProperty().bind(rowsValue.asString())
        rowsTextField.isEditable = false
        rowsTextField.prefWidth = 30.0
        add(rowsLabel, 0, 3)
        add(rowsSlider, 1, 3)
        add(rowsTextField, 2, 3)

        // Columns Slider
        val columnsLabel = Label("Columns:")
        val columnsSlider = Slider(4.0, 10.0, 7.0)
        columnsSlider.majorTickUnit = 1.0
        columnsSlider.minorTickCount = 0
        columnsSlider.isSnapToTicks = true
        columnsSlider.isShowTickMarks = true
        columnsSlider.isShowTickLabels = true
        columnsValue.bind(columnsSlider.valueProperty().asObject().map(Double::toInt))
        val columnsTextField = TextField()
        columnsTextField.textProperty().bind(columnsValue.asString())
        columnsTextField.isEditable = false
        columnsTextField.prefWidth = 30.0
        add(columnsLabel, 0, 4)
        add(columnsSlider, 1, 4)
        add(columnsTextField, 2, 4)

        // Line Slider
        val lineLabel = Label("Line Length:")
        val lineSlider = Slider(3.0, 7.0, 4.0)
        lineSlider.majorTickUnit = 1.0
        lineSlider.minorTickCount = 0
        lineSlider.isSnapToTicks = true
        lineSlider.isShowTickMarks = true
        lineSlider.isShowTickLabels = true
        linesValue.bind(lineSlider.valueProperty().asObject().map(Double::toInt))
        val linesTextField = TextField()
        linesTextField.textProperty().bind(linesValue.asString())
        linesTextField.isEditable = false
        linesTextField.prefWidth = 30.0
        add(lineLabel, 0, 5)
        add(lineSlider, 1, 5)
        add(linesTextField, 2, 5)

        // Add a separator to span all columns.
        val separator = Separator()
        add(separator, 0, 6, 3, 1)

        // Buttons for Play and Quit
        val playButton = Button("_Play")
        playButton.setOnAction {
            val setupResult = Play(
                rowsValue.get(),
                columnsValue.get(),
                linesValue.get(),
                humanPlayer1.get(),
                humanPlayer2.get(),
                if (regularGame.get()) GameType.REGULAR else GameType.MISERE
            )
            onSetupComplete(setupResult)
        }

        val quitButton = Button("_Quit")
        quitButton.setOnAction {
            onSetupComplete(Quit)
        }

        val buttonBox = HBox(10.0, playButton, quitButton)
        buttonBox.alignment = Pos.CENTER_RIGHT
        add(buttonBox, 0, 7, 3, 1)
        setVgrow(buttonBox, Priority.ALWAYS)
    }

    companion object {
        fun initialize(onSetupComplete: (SetupResult) -> Unit): SetupPanel {
            val humanPlayer1 = SimpleBooleanProperty(true)
            val humanPlayer2 = SimpleBooleanProperty(false)
            val regularGame = SimpleBooleanProperty(true)
            val rowsValue = SimpleIntegerProperty(6)
            val columnsValue = SimpleIntegerProperty(7)
            val linesValue = SimpleIntegerProperty(4)

            return SetupPanel(humanPlayer1, humanPlayer2, regularGame, rowsValue, columnsValue, linesValue, onSetupComplete)
        }
    }
}
