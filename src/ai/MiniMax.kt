package ai

import tic.tac.toe.enums.Player
import tic.tac.toe.models.Game
import tic.tac.toe.models.IModel
import tic.tac.toe.helpers.Result
import tic.tac.toe.helpers.Vector2

class MiniMax(var playsAs: Player, var maxDepth: Int = Integer.MAX_VALUE) {
    var otherPlayer = if (playsAs == Player.X) Player.O else Player.X


    fun predictBestTurn(game: Game): Vector2 {
        val clonedGame = game.miniMaxClone() as Game
        return maxTurn(clonedGame).move
    }


    private fun score(game: IModel, depth: Int): Int {
        if (game.hasPlayerWon(playsAs))
            return 10 - depth
        else if (game.hasPlayerWon(otherPlayer))
            return depth - 10

        return 0
    }

    private fun maxTurn(game: Game, depth: Int = 0): Result {
        if (depth == maxDepth || game.hasEnded()) {
            return Result(score(game, depth), Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE))
        }

        val max = Result(Integer.MIN_VALUE, Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE))

        for (i in (0 until game.boardSize)) {
            for (j in (0 until game.boardSize)) {
                val moveToMake = Vector2(i, j)

                if (game.isMoveLegal(moveToMake)) {
                    game.makeMove(moveToMake, playsAs)

                    val currentMove = minTurn(game, depth + 1)

                    if (currentMove.score > max.score) {
                        max.score = currentMove.score
                        max.move.x = i
                        max.move.y = j
                    }

                    game.makeMove(moveToMake, Player.None)
                }
            }
        }

        return max
    }


    private fun minTurn(game: Game, depth: Int = 0): Result {
        if (depth == maxDepth || game.hasEnded()) {
            return Result(score(game, depth), Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE))
        }

        val min = Result(Integer.MAX_VALUE, Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE))

        for (i in (0 until game.boardSize)) {
            for (j in (0 until game.boardSize)) {
                val moveToMake = Vector2(i, j)

                if (game.isMoveLegal(moveToMake)) {
                    game.makeMove(moveToMake, otherPlayer)

                    val currentMove = maxTurn(game, depth + 1)

                    if (currentMove.score < min.score) {
                        min.score = currentMove.score
                        min.move.x = i
                        min.move.y = j
                    }

                    game.makeMove(moveToMake, Player.None)
                }
            }
        }

        return min
    }
}