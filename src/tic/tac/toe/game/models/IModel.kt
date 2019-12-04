package tic.tac.toe.game.models

import tic.tac.toe.ai.MiniMax.DeepClonable
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.structures.Position

interface IModel : IObservable, DeepClonable {
    fun makeMove(move: Position, player: Player)
    fun isMovePossible(move: Position): Boolean
}