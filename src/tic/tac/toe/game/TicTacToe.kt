package tic.tac.toe.game

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tic.tac.toe.game.controllers.GameController
import tic.tac.toe.game.models.Game
import tic.tac.toe.game.views.GameView
import javafx.stage.Modality
import javafx.stage.StageStyle


class TicTacToe : Application() {
    lateinit var controller: GameController

    override fun start(primaryStage: Stage) {
        this.controller = GameController(Game(3, 3), GameView(500.0))
        val layout = GridPane()

        // Menus
        val gameMenu = Menu("Game")
        val gameMenuRestart = MenuItem("Restart")
        gameMenuRestart.setOnAction {
            controller.restart()
        }

        val gameMenuOptions = MenuItem("Options")
        gameMenuOptions.setOnAction {
            showOptionsDialog()
        }

        val gameMenuScoreboard = MenuItem("Scoreboard")
        gameMenuScoreboard.setOnAction {
            println("Display Scoreboard!")
        }


        gameMenu.items.add(gameMenuRestart)
        gameMenu.items.add(gameMenuOptions)
        gameMenu.items.add(gameMenuScoreboard)

        // MenuBar
        val menuBar = MenuBar()
        menuBar.menus.add(gameMenu)


        val vb = VBox(menuBar)
        layout.add(vb, 0, 0)


        // Game
        layout.add(controller.view, 0, 1)
        val scene = Scene(layout)

        primaryStage.title = "TicTacToe"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun showOptionsDialog() {
        val window = Stage()
        window.initModality(Modality.APPLICATION_MODAL)
        window.initStyle(StageStyle.UTILITY)

        window.title = "TicTacToe Options"
        window.width = 300.0
        window.height = 175.0

        val label = Label("Test")
        val button = Button("Apply")
        button.setOnAction {
            //controller = GameController(Game(4, 3), controller.view)
        }

        val root = GridPane()
        root.add(label, 0, 0)
        root.add(button, 0, 1)

        window.scene = Scene(root, 300.0, 175.0)

        window.showAndWait()
    }


    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            launch(TicTacToe::class.java, *args)
        }
    }
}