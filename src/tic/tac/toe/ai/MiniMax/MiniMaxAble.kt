package tic.tac.toe.ai.MiniMax

import tic.tac.toe.game.structures.Position

interface MiniMaxAble {
    fun score(move: Position): Int
    fun isInTerminalState(move: Position): Boolean
}