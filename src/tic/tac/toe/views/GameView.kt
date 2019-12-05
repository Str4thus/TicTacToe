package tic.tac.toe.views

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.util.Duration
import tic.tac.toe.controller.IController
import tic.tac.toe.enums.Player
import tic.tac.toe.helpers.Combo
import tic.tac.toe.helpers.Vector2
import tic.tac.toe.views.components.Tile

class GameView(private val windowSize: Double) : Pane(), IView {
    private lateinit var clickHandler: IController
    private val menuBarSizeBuffer: Double = 3.0

    override fun init(board: Array<Array<Player>>) {
        stylesheets.add(this::class.java.classLoader.getResource("styles.css").toExternalForm())
        setPrefSize(windowSize + menuBarSizeBuffer, windowSize + menuBarSizeBuffer)

        children.clear()
        for (i in (0 until board.size)) {
            for (j in (0 until board.size)) {
                val tile = Tile(i, j, windowSize / board.size)
                tile.translateX = i * tile.size
                tile.translateY = j * tile.size

                children.add(tile)
            }
        }
    }

    override fun displayWinner(combo: Combo) {
        val startTile: Tile = children.find { val tile = it as Tile; tile.x == combo.startPosition.x && tile.y == combo.startPosition.y } as Tile
        val endTile = children.find { val tile = it as Tile; tile.x == combo.endPosition.x && tile.y == combo.endPosition.y } as Tile

        val line = Line()
        line.styleClass.add("finish-line")
        line.strokeWidth = startTile.size / 15
        line.startX = startTile.screenX
        line.startY = startTile.screenY
        line.endX = startTile.screenX
        line.endY = startTile.screenY

        children.add(line)

        val timeLine = Timeline()
        timeLine.keyFrames.add(KeyFrame(Duration.seconds(0.5),
                KeyValue(line.endXProperty(), endTile.screenX),
                KeyValue(line.endYProperty(), endTile.screenY)))

        timeLine.play()
    }

    override fun setMark(position: Vector2, player: Player) {
        children.forEach {
            if (it is Tile && it.x == position.x && it.y == position.y) {
                when (player) {
                    Player.X -> it.drawX()
                    Player.O -> it.drawO()
                    else -> it.clear()
                }
            }
        }
    }


    override fun refuseMove(position: Vector2) {
        children.forEach {
            if (it is Tile && it.x == position.x && it.y == position.y) {
                it.refuseClick()
            }
        }
    }


    override fun addClickHandler(handler: IController) {
        this.clickHandler = handler

        children.forEach {
            if (it is Tile) {
                val tile: Tile = it
                it.setOnMouseClicked { clickHandler.handleClick(Vector2(tile.x, tile.y)) }
            }
        }
    }
}