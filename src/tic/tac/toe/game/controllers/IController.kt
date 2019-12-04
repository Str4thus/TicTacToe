package tic.tac.toe.game.controllers

import tic.tac.toe.ai.MiniMax.DeepClonable
import tic.tac.toe.ai.MiniMax.MiniMaxAble
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.structures.Position

interface IController : IModelObserver, DeepClonable, MiniMaxAble {
    fun getBoard(): Array<Array<Player>>
    fun hasPlayerWon(move: Position, player: Player): Boolean
    fun switchPlayer()
    fun makeMove(move: Position, player: Player)
    fun validateMove(move: Position): Boolean
    fun restart()
}