package tic.tac.toe.game.controllers

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import tic.tac.toe.ai.ArtificialIntelligence
import tic.tac.toe.ai.MiniMax.MiniMax
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.models.Game
import tic.tac.toe.game.structures.Combo
import tic.tac.toe.game.structures.Position
import tic.tac.toe.game.views.GameView
import tic.tac.toe.game.views.components.Tile


class GameController(var game: Game, var view: GameView, clone: Boolean = false) : IController {
    inner class ClickHandler : EventHandler<MouseEvent> {
        override fun handle(event: MouseEvent) {
            val clickedTile = event.source as Tile
            handleClickOnTile(Position(clickedTile.x, clickedTile.y))
        }
    }
    private val humanPlayer = Player.X
    private val aiPlayer = if (humanPlayer == Player.O) Player.X else Player.O
    private var ai: ArtificialIntelligence = MiniMax(aiPlayer, 3)

    private var winner: Player = Player.None
    private var currentPlayer: Player = humanPlayer

    init {
        if (!clone) {
            game.addObserver(this)
            view.init(game)
            view.addClickHandler(ClickHandler())
        }
    }

    fun handleClickOnTile(move: Position) {
        if (hasGameEnded()) {
            restart()
            return
        }

        var moveToPlay = move
        val playAgainstAi = true
        if (playAgainstAi && currentPlayer == ai.playsAs) {
            moveToPlay = ai.act(this)
            makeMove(moveToPlay, ai.playsAs)
        } else {
            if (validateMove(moveToPlay)) {
                makeMove(moveToPlay, currentPlayer)
            } else {

            }
        }

        if (hasPlayerWon(moveToPlay, currentPlayer)) {
            winner = currentPlayer
        }

        if (hasGameEnded()) {
            if (winner != Player.None) {
                val winningCombo = determineWinningCombo(moveToPlay)
                view.displayWinner(winningCombo)
            }
        } else {
            switchPlayer()

            if (currentPlayer == ai.playsAs) {
                handleClickOnTile(move)
            }
        }
    }

    private fun hasGameEnded(): Boolean {
        return !game.hasEmptyFields() || winner != Player.None
    }

    private fun checkForHorizontalLines(rootX: Int, rootY: Int, comboState: Player): Combo? {
        var leftX = rootX
        var rightX = rootX

        // Check left fields
        for (i in (rootX downTo 0)) {
            if (game.board[i][rootY] == comboState) {
                leftX = i
            } else {
                break
            }
        }

        // Check right fields
        for (i in (rootX until game.boardSize)) {
            if (game.board[i][rootY] == comboState) {
                rightX = i
            } else {
                break
            }
        }
        if (rightX - leftX >= game.winningComboCount - 1) {
            return Combo(Position(leftX, rootY), Position(rightX, rootY))
        }
        return null
    }

    private fun checkForVerticalLines(rootX: Int, rootY: Int, comboState: Player): Combo? {
        var topY = rootY
        var bottomY = rootY

        // Check top fields
        for (i in (rootY downTo 0)) {
            if (game.board[rootX][i] == comboState) {
                topY = i
            } else {
                break
            }
        }

        // Check bottom fields
        for (i in (rootY until game.boardSize)) {
            if (game.board[rootX][i] == comboState) {
                bottomY = i
            } else {
                break
            }
        }

        if (bottomY - topY >= game.winningComboCount - 1) {
            return Combo(Position(rootX, topY), Position(rootX, bottomY))
        }
        return null
    }

    private fun checkForDiagonalLines(rootX: Int, rootY: Int, comboState: Player): Combo? {
        var goesTopLeft = false // Diagonal can either be from top left to bottom right or vice versa.
        // To draw the finish line correctly, we need to keep track which direction it goes

        var leftX = rootX
        var rightX = rootX

        var topY = rootY
        var bottomY = rootY

        // Check top-left fields
        for (i in (1..Math.min(rootX, rootY))) {
            if (game.board[rootX - i][rootY - i] == comboState) {
                goesTopLeft = true // If one field is top left of the current one, the diagonal has to
                // go from top left to bottom right
                leftX = rootX - i
                topY = rootY - i
            } else {
                break
            }
        }

        // Check top-right fields
        for (i in (1..Math.min(game.boardSize - 1 - rootX, rootY))) {
            if (game.board[rootX + i][rootY - i] == comboState) {
                rightX = rootX + i
                topY = rootY - i
            } else {
                break
            }
        }

        // Check bottom-left fields
        for (i in (1..Math.min(rootX, game.boardSize - 1 - rootY))) {
            if (game.board[rootX - i][rootY + i] == comboState) {
                leftX = rootX - i
                bottomY = rootY + i
            } else {
                break
            }
        }

        // Check bottom-right fields
        for (i in (1..Math.min(game.boardSize - 1 - rootX, game.boardSize - 1 - rootY))) {
            if (game.board[rootX + i][rootY + i] == comboState) {
                goesTopLeft = true // if the diagonal goes bottom right, it implies cant go top right or bottom left
                rightX = rootX + i
                bottomY = rootY + i
            } else {
                break
            }
        }

        if (rightX - leftX >= game.winningComboCount - 1 && bottomY - topY >= game.winningComboCount - 1) {
            if (goesTopLeft) {
                return Combo(Position(leftX, topY), Position(rightX, bottomY))
            }
            return Combo(Position(leftX, bottomY), Position(rightX, topY))
        }
        return null
    }

    private fun determineWinningCombo(move: Position): Combo {
        var comboPoints = checkForHorizontalLines(move.x, move.y, winner)

        if (comboPoints == null) {
            comboPoints = checkForVerticalLines(move.x, move.y, winner)
            if (comboPoints == null) {
                comboPoints = checkForDiagonalLines(move.x, move.y, winner)
            }
        }
        return comboPoints!!
    }

    override fun hasPlayerWon(move: Position, player: Player): Boolean {
        return (checkForHorizontalLines(move.x, move.y, player) != null) || (checkForVerticalLines(move.x, move.y, player) != null)
                || (checkForDiagonalLines(move.x, move.y, player) != null)
    }

    override fun restart() {
        winner = Player.None
        game = Game(game.boardSize, game.winningComboCount)
        game.addObserver(this)
        view.refresh(game)
        view.addClickHandler(ClickHandler())
        currentPlayer = humanPlayer

        if (currentPlayer == ai.playsAs) {

            val moveToPlay = ai.act(this)
            makeMove(moveToPlay, ai.playsAs)

            switchPlayer()
        }
    }

    override fun switchPlayer() {
        currentPlayer = if (currentPlayer == Player.O) Player.X else Player.O
    }

    override fun makeMove(move: Position, player: Player) {
        game.makeMove(move, player)
    }

    override fun validateMove(move: Position): Boolean {
        return game.isMovePossible(move)
    }


    override fun isInTerminalState(move: Position): Boolean {
        return !game.hasEmptyFields() || hasPlayerWon(move, Player.X) || hasPlayerWon(move, Player.O)
    }

    override fun score(move: Position): Int {
        return ai.evaluate(move)
    }

    override fun deepClone(): GameController {
        val instance = GameController(game.deepClone(), view, true)
        instance.ai = ai
        instance.winner = winner
        instance.currentPlayer = currentPlayer

        return instance
    }

    override fun getBoard(): Array<Array<Player>> {
        return game.board
    }


    override fun update(position: Position) {
        view.setMark(position, currentPlayer)
    }
}