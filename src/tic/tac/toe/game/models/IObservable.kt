package tic.tac.toe.game.models

import tic.tac.toe.game.controllers.IModelObserver
import tic.tac.toe.game.structures.Position

interface IObservable {
    fun notifyChanges(position: Position)
    fun addObserver(observer: IModelObserver)
    fun removeObserver(observer: IModelObserver)
}