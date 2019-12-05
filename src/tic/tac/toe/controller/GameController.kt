package tic.tac.toe.controller

import ai.MiniMax
import tic.tac.toe.enums.Player
import tic.tac.toe.models.Game
import tic.tac.toe.helpers.Vector2
import tic.tac.toe.views.GameView

class GameController(private var game: Game, val gameView: GameView) : IController {
    private var againstAi = false
    private var ai = MiniMax(Player.O)

    init {
        initViewWithModel()
    }

    private fun initViewWithModel() {
        game.addObserver(this)
        gameView.init(game.board)
        gameView.addClickHandler(this)
    }

    private fun performAiTurn() {
        game.makeMove(ai.predictBestTurn(game), game.currentPlayer)

        if (game.hasPlayerWon(game.currentPlayer)) {
            gameView.displayWinner(game.winningCombo!!)
            return
        }

        game.switchPlayer()
    }


    // For restarting without changes
    fun restart() {
        restart(game.boardSize, game.winningComboCount)
    }

    override fun restart(newSize: Int, newWinningComboCount: Int) {
        game = Game(newSize, newWinningComboCount)
        initViewWithModel()
    }


    override fun toggleAI() {
        againstAi = !againstAi
        restart()
    }

    override fun handleClick(position: Vector2) {
        if (game.hasEnded()) {
            restart()
            return
        }

        if (game.isMoveLegal(position)) {
            game.makeMove(position, game.currentPlayer)

            if (game.hasPlayerWon(game.currentPlayer)) {
                gameView.displayWinner(game.winningCombo!!)
                return
            }

            if (!game.hasEnded()) {
                game.switchPlayer()

                if (againstAi) {
                    performAiTurn()
                }
            }
        } else {
            gameView.refuseMove(position)
        }


    }

    override fun updateView(position: Vector2, player: Player) {
        gameView.setMark(position, player)
    }
}