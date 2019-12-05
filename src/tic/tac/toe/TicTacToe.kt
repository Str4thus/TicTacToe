package tic.tac.toe

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import tic.tac.toe.controller.GameController
import tic.tac.toe.models.Game
import tic.tac.toe.views.GameView

class TicTacToe : Application() {

    override fun start(primaryStage: Stage) {
        val controller = GameController(Game(3, 3), GameView(500.0))
        val grid = GridPane()

        val menuBar = MenuBar()
        val gameMenu = Menu("Game")
        val gameMenuRestart = MenuItem("Restart")
        gameMenuRestart.setOnAction { controller.restart() }

        val gameMenuToggleAI = MenuItem("Toggle AI")
        gameMenuToggleAI.setOnAction { controller.toggleAI() }


        val gameMenuScoreboard = MenuItem("Scoreboard")
        gameMenuScoreboard.setOnAction {println("Show Scoreboard!") }

        gameMenu.items.add(gameMenuRestart)
        gameMenu.items.add(gameMenuToggleAI)
        gameMenu.items.add(gameMenuScoreboard)

        menuBar.menus.add(gameMenu)

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