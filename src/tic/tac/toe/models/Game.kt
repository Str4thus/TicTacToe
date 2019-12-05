package tic.tac.toe.models

import ai.IMiniMaxClone
import tic.tac.toe.enums.Player
import tic.tac.toe.helpers.Combo
import tic.tac.toe.helpers.IModelObserver
import tic.tac.toe.helpers.Vector2

open class Game(var boardSize: Int, var winningComboCount: Int) : IModel {
    var board = Array(boardSize, { Array(boardSize, { Player.None }) })
    var currentPlayer = Player.X
    var winningCombo: Combo? = null

    private var lastMove: Vector2? = null
    private val observers = ArrayList<IModelObserver>()


    private fun checkForHorizontalLines(comboState: Player): Boolean {
        if (board[lastMove!!.x][lastMove!!.y] != comboState)
            return false

        var leftX = lastMove!!.x
        var rightX = lastMove!!.x

        // Check left fields
        for (i in (lastMove!!.x downTo 0)) {
            if (board[i][lastMove!!.y] == comboState) {
                leftX = i
            } else {
                break
            }
        }

        // Check right fields
        for (i in (lastMove!!.x until boardSize)) {
            if (board[i][lastMove!!.y] == comboState) {
                rightX = i
            } else {
                break
            }
        }
        if (rightX - leftX >= winningComboCount - 1) {
            winningCombo = Combo(Vector2(leftX, lastMove!!.y), Vector2(rightX, lastMove!!.y))
            return true
        }
        return false
    }

    private fun checkForVerticalLines(comboState: Player): Boolean {
        if (board[lastMove!!.x][lastMove!!.y] != comboState)
            return false

        var topY = lastMove!!.y
        var bottomY = lastMove!!.y

        // Check top fields
        for (i in (lastMove!!.y downTo 0)) {
            if (board[lastMove!!.x][i] == comboState) {
                topY = i
            } else {
                break
            }
        }

        // Check bottom fields
        for (i in (lastMove!!.y until boardSize)) {
            if (board[lastMove!!.x][i] == comboState) {
                bottomY = i
            } else {
                break
            }
        }

        if (bottomY - topY >= winningComboCount - 1) {
            winningCombo = Combo(Vector2(lastMove!!.x, topY), Vector2(lastMove!!.x, bottomY))
            return true
        }
        return false
    }

    private fun checkForDiagonalLines(comboState: Player): Boolean {
        if (board[lastMove!!.x][lastMove!!.y] != comboState)
            return false

        var goesTopLeft = false // Diagonal can either be from top left to bottom right or vice versa.
        // To draw the finish line correctly, we need to keep track which direction it goes

        var leftX = lastMove!!.x
        var rightX = lastMove!!.x

        var topY = lastMove!!.y
        var bottomY = lastMove!!.y

        // Check top-left fields
        for (i in (1..Math.min(lastMove!!.x, lastMove!!.y))) {
            if (board[lastMove!!.x - i][lastMove!!.y - i] == comboState) {
                goesTopLeft = true // If one field is top left of the current one, the diagonal has to
                // go from top left to bottom right
                leftX = lastMove!!.x - i
                topY = lastMove!!.y - i
            } else {
                break
            }
        }

        // Check top-right fields
        for (i in (1..Math.min(boardSize - 1 - lastMove!!.x, lastMove!!.y))) {
            if (board[lastMove!!.x + i][lastMove!!.y - i] == comboState) {
                rightX = lastMove!!.x + i
                topY = lastMove!!.y - i
            } else {
                break
            }
        }

        // Check bottom-left fields
        for (i in (1..Math.min(lastMove!!.x, boardSize - 1 - lastMove!!.y))) {
            if (board[lastMove!!.x - i][lastMove!!.y + i] == comboState) {
                leftX = lastMove!!.x - i
                bottomY = lastMove!!.y + i
            } else {
                break
            }
        }

        // Check bottom-right fields
        for (i in (1..Math.min(boardSize - 1 - lastMove!!.x, boardSize - 1 - lastMove!!.y))) {
            if (board[lastMove!!.x + i][lastMove!!.y + i] == comboState) {
                goesTopLeft = true // if the diagonal goes bottom right, it implies cant go top right or bottom left
                rightX = lastMove!!.x + i
                bottomY = lastMove!!.y + i
            } else {
                break
            }
        }

        if (rightX - leftX >= winningComboCount - 1 && bottomY - topY >= winningComboCount - 1) {
            winningCombo = Combo(Vector2(leftX, bottomY), Vector2(rightX, topY))
            if (goesTopLeft) {
                winningCombo = Combo(Vector2(leftX, topY), Vector2(rightX, bottomY))
            }
            return true
        }
        return false
    }

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


    // IModelObservable
    override fun notifyChanges(position: Vector2, player: Player) {
        observers.forEach { it.updateView(position, player) }
    }

    override fun addObserver(observer: IModelObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: IModelObserver) {
        observers.remove(observer)
    }


    // IModel
    override fun makeMove(move: Vector2, player: Player) {
        lastMove = move
        board[move.x][move.y] = player
        notifyChanges(move, player)
    }

    override fun switchPlayer() {
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
    }

    override fun isMoveLegal(move: Vector2): Boolean {
        return board[move.x][move.y] == Player.None
    }

    override fun hasPlayerWon(player: Player): Boolean {
        return lastMove != null && (checkForHorizontalLines(player)
                || checkForVerticalLines(player)
                || checkForDiagonalLines(player))
    }

    override fun hasEnded(): Boolean {
        return !hasEmptyFields() || hasPlayerWon(Player.X) || hasPlayerWon(Player.O)
    }

    // IMiniMaxClone
    override fun miniMaxClone(): IMiniMaxClone {
        val instance = Game(boardSize, winningComboCount)
        val clonedBoard = Array(boardSize, { Array(boardSize, { Player.None }) })

        for (i in (0 until boardSize)) {
            for (j in (0 until boardSize)) {
                clonedBoard[i][j] = board[i][j]
            }
        }

        instance.board = clonedBoard
        instance.lastMove = if(lastMove != null) lastMove else Vector2(0, 0)
        instance.currentPlayer = currentPlayer

        return instance
    }
}