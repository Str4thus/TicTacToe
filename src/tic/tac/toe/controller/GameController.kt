package tic.tac.toe.controller

import ai.MiniMax
import tic.tac.toe.enums.Player
import tic.tac.toe.models.Game
import tic.tac.toe.helpers.Vector2
import tic.tac.toe.views.GameView

class GameController(private var game: Game, val gameView: GameView) : IController {
    var againstAi = false
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


    fun getBoardSize(): Int {
        return game.boardSize
    }

    fun getWinningComboCount(): Int {
        return game.winningComboCount
    }

    fun adjustAIDepth(maxDepth: Int) {
        if (maxDepth == 0) {
            this.ai = MiniMax(ai.playsAs)
            return
        }
        this.ai = MiniMax(ai.playsAs, maxDepth = maxDepth)
    }

    fun getAIDepth(): Int {
        if (ai.maxDepth == Integer.MAX_VALUE) {
            return 0
        }
        return ai.maxDepth
    }

    // For restarting without changes
    fun restart(newSize: Int = game.boardSize) {
        restart(newSize, game.winningComboCount)
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