package tic.tac.toe.models

import ai.IMiniMaxClone
import tic.tac.toe.enums.Player
import tic.tac.toe.helpers.IModelObservable
import tic.tac.toe.helpers.Vector2

interface IModel : IMiniMaxClone, IModelObservable {
    fun makeMove(move: Vector2, player: Player)
    fun switchPlayer()

    fun hasPlayerWon(player: Player): Boolean
    fun isMoveLegal(move: Vector2): Boolean
    fun hasEnded(): Boolean
}