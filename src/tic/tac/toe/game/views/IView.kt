package tic.tac.toe.game.views

import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.models.IModel
import tic.tac.toe.game.structures.Combo
import tic.tac.toe.game.structures.Position

interface IView {
    fun init(model: IModel)
    fun refresh(model: IModel)
    fun setMark(tile: Position, player: Player)
    fun displayWinner(combo: Combo)
}