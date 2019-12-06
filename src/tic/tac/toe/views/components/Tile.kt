package tic.tac.toe.views.components

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font

class Tile(val x: Int, val y: Int, val size: Double) : StackPane() {
    val screenX: Double = (x + 1) * size - size / 2
    val screenY: Double = y * size + size / 2
    private val label = Label()

    init {
        alignment = Pos.TOP_LEFT
        val rect = Rectangle(size, size)

        rect.styleClass.add("tile")
        rect.strokeWidth = 3.0

        label.styleClass.add("tile-label")
        label.maxWidth = Double.MAX_VALUE
        label.maxHeight = Double.MAX_VALUE
        label.alignment = Pos.CENTER
        label.font = Font.font(size / 2)

        children.addAll(rect, label)

        addEventFilter(MouseEvent.MOUSE_ENTERED, {
            if (label.text.isEmpty()) {
                rect.styleClass.add("hovered")
            }
        })
        addEventFilter(MouseEvent.MOUSE_PRESSED, {
            rect.styleClass.remove("hovered")
        })

        addEventFilter(MouseEvent.MOUSE_EXITED, { rect.styleClass.remove("hovered") })
    }

    fun drawO() {
        label.styleClass.add("o")
        label.text = "O"
    }

    fun drawX() {
        label.styleClass.add("x")
        label.text = "X"
    }

    fun clear() {
        label.styleClass.remove("x")
        label.styleClass.remove("o")
        label.text = "X"
    }

    fun refuseClick() {
        label.styleClass.add("refused")
        val thread = Thread(Runnable {
            val updater = Runnable { label.styleClass.remove("refused") }

            try {
                Thread.sleep(500)
            } catch (ex: InterruptedException) {
            }

            Platform.runLater(updater)

        })

        thread.start()
    }
}