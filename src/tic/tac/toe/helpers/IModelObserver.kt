package tic.tac.toe.helpers

import tic.tac.toe.enums.Player

interface IModelObserver {
    fun updateView(position: Vector2, player: Player)
}