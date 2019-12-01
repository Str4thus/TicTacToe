package tic.tac.toe.ai.MiniMax

import tic.tac.toe.ai.ArtificialIntelligence
import tic.tac.toe.game.controllers.IController
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.structures.Position

class MiniMax(override val playsAs: Player, val maxDepth: Int = Integer.MAX_VALUE) : ArtificialIntelligence() {
    override val otherPlayer = if (playsAs == Player.X) Player.O else Player.X
    private var depth = 0

    override fun act(game: IController): Position {
        this.controller = game.deepClone() as IController
        return calculateAction()
    }

    override fun evaluate(move: Position): Int {
        if (controller.hasPlayerWon(move, playsAs)) {
            return 10 - depth
        } else if (controller.hasPlayerWon(move, otherPlayer)) {
            return depth - 10
        }

        return 0
    }

    override fun calculateAction(): Position {
        return maxTurn(this.controller).move!!
    }

    private fun maxTurn(controller: IController, depth: Int = 0, move_: Position? = null): Result {
        if (move_ != null && (depth == maxDepth || controller.isInTerminalState(move_))) {
            return Result(controller.score(move_), null)
        }

        this.depth++
        val max = Result(Integer.MIN_VALUE, Position(Integer.MAX_VALUE, Integer.MAX_VALUE))

        val board = controller.getBoard()
        val boardSize = board.size
        for (i in (0 until boardSize)) {
            for (j in (0 until boardSize)) {
                val move = Position(i, j)
                if (controller.validateMove(move)) {
                    controller.makeMove(move, playsAs)

                    val currentMove = minTurn(controller, depth + 1, move)

                    if (currentMove.score > max.score) {
                        max.score = currentMove.score
                        max.move = move
                    }

                    controller.makeMove(move, Player.None)
                }
            }
        }

        this.depth--
        return max
    }

    private fun minTurn(controller: IController, depth: Int = 0, move_: Position? = null): Result {
        if (move_ != null && (depth == maxDepth || controller.isInTerminalState(move_))) {
            return Result(controller.score(move_), null)
        }

        this.depth++
        val min = Result(Integer.MAX_VALUE, null)

        val board = controller.getBoard()
        val boardSize = board.size
        for (i in (0 until boardSize)) {
            for (j in (0 until boardSize)) {
                val move = Position(i, j)

                if (controller.validateMove(move)) {
                    controller.makeMove(move, otherPlayer)

                    val currentMove = maxTurn(controller, depth + 1, move)

                    if (currentMove.score < min.score) {
                        min.score = currentMove.score
                        min.move = move
                    }

                    controller.makeMove(move, Player.None)
                }
            }
        }

        this.depth--
        return min
    }
}