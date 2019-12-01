package tic.tac.toe.game.views

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.util.Duration
import tic.tac.toe.game.enums.Player
import tic.tac.toe.game.models.Game
import tic.tac.toe.game.models.IModel
import tic.tac.toe.game.structures.Combo
import tic.tac.toe.game.structures.Position
import tic.tac.toe.game.views.components.Tile

class GameView(private val windowSize: Double) : Pane(), IView {
    private var clickHandler: EventHandler<MouseEvent>? = null

    override fun init(model: IModel) {
        stylesheets.add(this::class.java.classLoader.getResource("styles.css").toExternalForm())
        setPrefSize(windowSize, windowSize)
        refresh(model)
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

    override fun setMark(tile: Position, player: Player) {
        children.forEach {
            if (it is Tile && it.x == tile.x && it.y == tile.y) {
                when (player) {
                    Player.X -> it.drawX()
                    Player.O -> it.drawO()
                    else -> it.clear()
                }
            }
        }
    }

    override fun refresh(model: IModel) {
        val game = model as Game

        children.clear()

        for (i in (0 until game.boardSize)) {
            for (j in (0 until game.boardSize)) {
                val tile = Tile(i, j, windowSize / game.boardSize)
                tile.translateX = i * tile.size
                tile.translateY = j * tile.size
                tile.onMouseClicked = clickHandler

                children.add(tile)
            }
        }
    }


    fun addClickHandler(handler: EventHandler<MouseEvent>?) {
        this.clickHandler = handler
        children.forEach {
            if (it is Tile) {
                it.onMouseClicked = clickHandler
            }
        }
    }
}