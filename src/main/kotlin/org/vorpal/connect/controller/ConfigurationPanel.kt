//package org.vorpal.connect.controller
//
//import javafx.application.Application
//import javafx.beans.property.SimpleIntegerProperty
//import javafx.geometry.Insets
//import javafx.geometry.Pos
//import javafx.scene.Scene
//import javafx.scene.control.Label
//import javafx.scene.control.Slider
//import javafx.scene.control.ToggleButton
//import javafx.scene.control.ToggleGroup
//import javafx.scene.layout.GridPane
//import javafx.scene.layout.HBox
//import javafx.scene.layout.VBox
//import javafx.stage.Stage
//
//class MainKt : Application() {
//
//    override fun start(primaryStage: Stage) {
//        val player1Label = Label("Player 1:")
//        val player1ToggleGroup = ToggleGroup()
//        val player1HumanToggle = ToggleButton("Human")
//        player1HumanToggle.toggleGroup = player1ToggleGroup
//        val player1ComputerToggle = ToggleButton("Computer")
//        player1ComputerToggle.toggleGroup = player1ToggleGroup
//        player1HumanToggle.isSelected = true
//        val player1ToggleBox = HBox(10.0, player1HumanToggle, player1ComputerToggle)
//        player1ToggleBox.alignment = Pos.TOP_LEFT
//
//        val player2Label = Label("Player 2:")
//        val player2ToggleGroup = ToggleGroup()
//        val player2HumanToggle = ToggleButton("Human")
//        player2HumanToggle.toggleGroup = player2ToggleGroup
//        val player2ComputerToggle = ToggleButton("Computer")
//        player2ComputerToggle.toggleGroup = player2ToggleGroup
//        val player2ToggleBox = HBox(10.0, player2HumanToggle, player2ComputerToggle)
//        player2HumanToggle.isSelected = true
//        player2ToggleBox.alignment = Pos.TOP_LEFT
//
//        val gameplayLabel = Label("Gameplay:")
//        val gameplayToggleGroup = ToggleGroup()
//        val gameplayRegularToggle = ToggleButton("Regular")
//        gameplayRegularToggle.toggleGroup = gameplayToggleGroup
//        val gameplayMisereToggle = ToggleButton("Misere")
//        gameplayMisereToggle.toggleGroup = gameplayToggleGroup
//        val gameplayToggleBox = HBox(10.0, gameplayRegularToggle, gameplayMisereToggle)
//        gameplayRegularToggle.isSelected = true
//        gameplayToggleBox.alignment = Pos.TOP_LEFT
//
//        val rowsLabel = Label("Rows:")
//        val rowsSlider = Slider(ROWS_MIN.toDouble(), ROWS_MAX.toDouble(), ROWS_DEFAULT.toDouble())
//        rowsSlider.majorTickUnit = 1.0
//        rowsSlider.minorTickCount = 0
//        rowsSlider.isSnapToTicks = true
//        rowsSlider.isShowTickMarks = true
//        rowsSlider.isShowTickLabels = true
//        val rowsValue = SimpleIntegerProperty(ROWS_DEFAULT)
//        rowsValue.bind(rowsSlider.valueProperty().asObject().map(Double::toInt))
//
//        // Columns Slider
//        val columnsLabel = Label("Columns:")
//        val columnsSlider = Slider(COLS_MIN.toDouble(), COLS_MAX.toDouble(), COLS_DEFAULT.toDouble())
//        columnsSlider.majorTickUnit = 1.0
//        columnsSlider.minorTickCount = 0
//        columnsSlider.isSnapToTicks = true
//        columnsSlider.isShowTickMarks = true
//        columnsSlider.isShowTickLabels = true
//        val columnsValue = SimpleIntegerProperty(COLS_DEFAULT)
//        columnsValue.bind(columnsSlider.valueProperty().asObject().map(Double::toInt))
//
//        // Line Slider
//        val lineLabel = Label("Line Length:")
//        val lineSlider = Slider(LINE_LENGTH_MIN.toDouble(), Math.max(COLS_DEFAULT, ROWS_DEFAULT).toDouble(), LINE_LENGTH_DEFAULT.toDouble())
//        lineSlider.majorTickUnit = 1.0
//        lineSlider.minorTickCount = 0
//        lineSlider.isSnapToTicks = true
//        lineSlider.isShowTickMarks = true
//        lineSlider.isShowTickLabels = true
//
//        // Update lineSlider based on rowsSlider and columnsSlider values
//        rowsSlider.valueProperty().addListener { _, _, newValue ->
//            lineSlider.max = newValue.toDouble().coerceAtLeast(columnsSlider.value)
//        }
//        columnsSlider.valueProperty().addListener { _, _, newValue ->
//            lineSlider.max = newValue.toDouble().coerceAtLeast(rowsSlider.value)
//        }
//
//        // Layout
//        val gridPane = GridPane().apply {
//            hgap = 10.0
//            vgap = 10.0
//            padding = Insets(20.0)
//            add(player1Label, 0, 0)
//            add(player1ToggleBox, 1, 0)
//            add(player2Label, 0, 1)
//            add(player2ToggleBox, 1, 1)
//            add(gameplayLabel, 0, 2)
//            add(gameplayToggleBox, 1, 2)
//            add(rowsLabel, 0, 3)
//            add(rowsSlider, 1, 3)
//
//            add(columnsLabel, 0, 4)
//            add(columnsSlider, 1, 4)
//            add(lineLabel, 0, 5)
//            add(lineSlider, 1, 5)
//        }
//
//        // Main container
//        val root = VBox(20.0, gridPane).apply {
//            alignment = Pos.TOP_LEFT
//            padding = Insets(20.0)
//        }
//
//        // Set up the stage
//        primaryStage.title = "Connect Four Setup"
//        primaryStage.scene = Scene(root, 400.0, 400.0)
//        primaryStage.show()
//    }
//
//    companion object {
//        const val ROWS_MIN = 4
//        const val ROWS_DEFAULT = 6
//        const val ROWS_MAX = 10
//
//        const val COLS_MIN = 4
//        const val COLS_DEFAULT = 7
//        const val COLS_MAX = 10
//
//        const val LINE_LENGTH_MIN = 3
//        const val LINE_LENGTH_DEFAULT = 4
//    }
//}
//
//fun main() {
//    Application.launch(MainKt::class.java)
//}
//
