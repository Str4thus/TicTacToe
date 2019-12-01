package tic.tac.toe.ai

import tic.tac.toe.game.controllers.IController
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.structures.Position

abstract class ArtificialIntelligence {
    lateinit var controller: IController
    abstract val playsAs: Player
    abstract val otherPlayer: Player

    abstract fun act(game: IController): Position
    abstract fun evaluate(move: Position): Int

    protected abstract fun calculateAction(): Position
}