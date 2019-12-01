package tic.tac.toe.game.controllers

import tic.tac.toe.game.structures.Position

interface IModelObserver {
    fun update(position: Position)
}