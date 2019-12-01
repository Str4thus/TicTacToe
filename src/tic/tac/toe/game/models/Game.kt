package tic.tac.toe.game.models

import tic.tac.toe.game.controllers.IModelObserver
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.structures.Position
import java.util.ArrayList

class Game(val boardSize: Int, val winningComboCount: Int) : IModel {
    var board: Array<Array<Player>> = createBoard(boardSize)
        private set
    private val observers: ArrayList<IModelObserver> = ArrayList()


    fun hasEmptyFields(): Boolean {
        for (i in (0 until boardSize)) {
            for (j in (0 until boardSize)) {
                if (board[i][j] == Player.None) {
                    return true
                }
            }
        }

        return false
    }

    private fun createBoard(boardSize: Int): Array<Array<Player>> {
        return Array(boardSize, {Array(boardSize, {Player.None})})
    }


    override fun isMovePossible(move: Position): Boolean {
        return board[move.x][move.y] == Player.None
    }

    override fun makeMove(move: Position, player: Player) {
        board[move.x][move.y] = player
        notifyChanges(move)
    }

    override fun addObserver(observer: IModelObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: IModelObserver) {
        observers.remove(observer)
    }

    override fun notifyChanges(position: Position) {
        observers.forEach { it.update(position) }
    }


    override fun deepClone(): Game {
        val instance = Game(boardSize, winningComboCount)
        val clonedBoard = Array(boardSize, {Array(boardSize, {Player.None})})

        for (i in (0 until boardSize)) {
            for (j in (0 until boardSize)) {
                when(board[i][j]) {
                    Player.X -> clonedBoard[i][j] = Player.X
                    Player.O -> clonedBoard[i][j] = Player.O
                    Player.None -> clonedBoard[i][j] = Player.None
                }
            }
        }
        instance.board = clonedBoard

        return instance
    }
}