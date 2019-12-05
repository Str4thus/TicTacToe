package tic.tac.toe.controller

import tic.tac.toe.helpers.IModelObserver
import tic.tac.toe.helpers.Vector2

interface IController : IModelObserver {
    fun handleClick(position: Vector2)
    fun restart(newSize: Int, newWinningComboCount: Int)
    fun toggleAI()
}