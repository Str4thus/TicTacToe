package tic.tac.toe.views

import tic.tac.toe.controller.IController
import tic.tac.toe.enums.Player
import tic.tac.toe.helpers.Combo
import tic.tac.toe.helpers.Vector2

interface IView {
    fun init(board: Array<Array<Player>>)
    fun setMark(position: Vector2, player: Player)
    fun displayWinner(combo: Combo)
    fun refuseMove(position: Vector2)
    fun addClickHandler(handler: IController)
}