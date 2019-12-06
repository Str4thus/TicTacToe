package tic.tac.toe

import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import tic.tac.toe.controller.GameController
import tic.tac.toe.models.Game
import tic.tac.toe.views.GameView


class TicTacToe : Application() {

    override fun start(primaryStage: Stage) {
        val controller = GameController(Game(3, 3), GameView(500.0))

        val menuBar = MenuBar()
        val gameMenu = Menu("Game")

        val gameMenuRestart = MenuItem("Restart")
        gameMenuRestart.setOnAction { controller.restart() }

        val gameMenuOptions = MenuItem("Options")
        gameMenuOptions.setOnAction {
            val window = Stage()
            window.initModality(Modality.APPLICATION_MODAL)
            window.initStyle(StageStyle.UTILITY)
            window.title = "Options"
            window.width = 400.0
            window.height = 250.0

            val boardSizeLabel = Label("Board Size")
            val boardSizeTextField = TextField(Integer.toString(controller.getBoardSize()))
            boardSizeTextField.setOnMouseClicked { _ -> boardSizeTextField.selectAll() }
            boardSizeTextField.textProperty().addListener { _, _, newValue ->
                if (!newValue.matches("\\d*".toRegex())) {
                    boardSizeTextField.text = newValue.replace("[^\\d]".toRegex(), "")
                }
            }

            val winningComboCountLabel = Label("Winning Combo Count")
            val winningComboCountTextField = TextField(Integer.toString(controller.getWinningComboCount()))
            winningComboCountTextField.setOnMouseClicked { _ -> winningComboCountTextField.selectAll() }
            winningComboCountTextField.textProperty().addListener { _, _, newValue ->
                if (!newValue.matches("\\d*".toRegex())) {
                    boardSizeTextField.text = newValue.replace("[^\\d]".toRegex(), "")
                }
            }

            val againstAICheckBox = CheckBox("Enable AI Opponent")
            againstAICheckBox.isSelected = controller.againstAi
            againstAICheckBox.selectedProperty().addListener { _, _, newValue ->
                controller.againstAi = newValue
            }

            val miniMaxDepthLabel = Label("AI Prediction Depth\n(0 for no restrictions, only for 3x3)")
            val miniMaxDepth = TextField(Integer.toString(controller.getAIDepth()))
            miniMaxDepth.setOnMouseClicked { _ -> miniMaxDepth.selectAll() }
            miniMaxDepth.disableProperty().bind(!againstAICheckBox.selectedProperty())
            miniMaxDepth.textProperty().addListener { _, _, newValue ->
                if (!newValue.matches("\\d*".toRegex())) {
                    miniMaxDepth.text = newValue.replace("[^\\d]".toRegex(), "")
                }
            }


            val errorField = Label()
            errorField.styleClass.add("error-box")

            val applyButton = Button("Apply")
            applyButton.setOnAction {
                val newBoardSize = Integer.parseInt(boardSizeTextField.text)
                val newWinningComboCount = Integer.parseInt(winningComboCountTextField.text)
                val newMiniMaxDepth = Integer.parseInt(miniMaxDepth.text)

                errorField.text = ""
                if (newBoardSize < 3 || newBoardSize > 10) {
                    errorField.text += "- Invalid board size!\n"
                }
                if (newWinningComboCount > newBoardSize){
                    errorField.text += "- Winning Combo Count cannot be greater than board size!\n"
                }
                if (newWinningComboCount < 2) {
                    errorField.text += "- Winning Combo Count cannot be smaller than 2!\n"
                }
                if (againstAICheckBox.isSelected && newBoardSize > 3 && (newMiniMaxDepth > 4 || newMiniMaxDepth == 0)) {
                    errorField.text += "- The depth leads to too high computation time!\n"
                }

                if (errorField.text.isEmpty()) {
                    controller.adjustAIDepth(newMiniMaxDepth)
                    controller.restart(newBoardSize, newWinningComboCount)
                    window.close()
                }
            }


            val root = GridPane()
            root.hgap = 30.0
            root.stylesheets.add(this::class.java.classLoader.getResource("options.css").toExternalForm())
            root.add(boardSizeLabel, 0, 1)
            root.add(boardSizeTextField, 1, 1)

            root.add(winningComboCountLabel, 0, 2)
            root.add(winningComboCountTextField, 1, 2)

            root.add(againstAICheckBox, 0, 3)
            root.add(miniMaxDepthLabel, 0, 4)
            root.add(miniMaxDepth, 1, 4)

            root.add(errorField, 0, 5)
            root.add(applyButton, 0, 6)

            window.scene = Scene(root)
            window.showAndWait()
        }

        val gameMenuScoreboard = MenuItem("Scoreboard")
        gameMenuScoreboard.setOnAction {println("Show Scoreboard!") }

        gameMenu.items.add(gameMenuRestart)
        gameMenu.items.add(gameMenuOptions)
        gameMenu.items.add(gameMenuScoreboard)
        menuBar.menus.add(gameMenu)


        val grid = GridPane()
        grid.add(menuBar, 0, 0)
        grid.add(controller.gameView, 0, 1)

        val scene = Scene(grid)
        primaryStage.title = "TicTacToe"
        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(TicTacToe::class.java, *args)
        }
    }
}