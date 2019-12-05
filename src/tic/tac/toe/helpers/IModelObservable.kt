package tic.tac.toe.helpers

import tic.tac.toe.enums.Player

interface IModelObservable {
    fun notifyChanges(position: Vector2, player: Player)
    fun addObserver(observer: IModelObserver)
    fun removeObserver(observer: IModelObserver)
}