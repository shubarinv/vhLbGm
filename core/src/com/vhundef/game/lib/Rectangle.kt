package com.vhundef.game.lib

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle

class Rect(x: Float, y: Float, width: Float, height: Float, var color: Color) : Rectangle() {

    private var texture: Texture? = null

    init {
        createTexture(width.toInt(), height.toInt(), color)
        setX(x)
        setY(y)
        setWidth(width)
        setHeight(height)
    }

    private fun createTexture(width: Int, height: Int, color: Color) {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fillRectangle(0, 0, width, height)
        texture = Texture(pixmap)
        pixmap.dispose()
    }

    fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(texture, x, y, width, height)
    }
}