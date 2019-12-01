package tic.tac.toe.game

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import tic.tac.toe.game.controllers.GameController
import tic.tac.toe.game.models.Game
import tic.tac.toe.game.views.GameView

class TicTacToe : Application() {
    override fun start(primaryStage: Stage) {
        val controller = GameController(Game(3, 3), GameView(500.0))
        val scene = Scene(controller.view)

        primaryStage.title = "TicTacToe"
        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            launch(TicTacToe::class.java, *args)
        }
    }
}