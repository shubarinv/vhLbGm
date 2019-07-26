// Maze generator in Kotlin
// Joe Wingbermuehle
// 2015-07-25

package com.vhundef.game.lib

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class Maze(val width: Int, val height: Int) {

    private enum class Cell {
        WALL, SPACE, VISITED
    }

    private val data = Array(width) { i ->
        Array<Cell>(height) { i -> Cell.WALL }
    }

    private val rects = Array(width) { i ->
        Array<Rectangle>(height) { i -> Rectangle(1f, 1f, 1f, 1f, Color.WHITE) }
    }

    private val rand = java.util.Random()

    init {
        generate()
    }

    private fun carve(x: Int, y: Int) {

        val upx = intArrayOf(1, -1, 0, 0)
        val upy = intArrayOf(0, 0, 1, -1)

        var dir = rand.nextInt(4)
        var count = 0
        while (count < 4) {
            val x1 = x + upx[dir]
            val y1 = y + upy[dir]
            val x2 = x1 + upx[dir]
            val y2 = y1 + upy[dir]
            if (data[x1][y1] == Cell.WALL && data[x2][y2] == Cell.WALL) {
                data[x1][y1] = Cell.SPACE
                data[x2][y2] = Cell.SPACE
                carve(x2, y2)
            } else {
                dir = (dir + 1) % 4
                count += 1
            }
        }

    }

    fun generate() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                data[x][y] = Cell.WALL
            }
        }

        for (x in 0 until width) {
            data[x][0] = Cell.SPACE
            data[x][height - 1] = Cell.SPACE
        }
        for (y in 0 until height) {
            data[0][y] = Cell.SPACE
            data[width - 1][y] = Cell.SPACE
        }

        data[2][2] = Cell.SPACE
        carve(2, 2)

        data[2][1] = Cell.SPACE
        data[width - 3][height - 2] = Cell.SPACE
    }

    fun draw(batch: SpriteBatch) {
        val rectSize = Gdx.graphics.width / width
        val rectYOffset = (Gdx.graphics.height - rectSize * height) - (rectSize * height) / 2
        for (y in 0 until height) {
            for (x in 0 until width) {
                rects[x][y].x = (x * rectSize).toFloat()
                rects[x][y].y = rectYOffset + (y * rectSize).toFloat()
                rects[x][y].width = (rectSize).toFloat()
                rects[x][y].height = (rectSize).toFloat()
                if (data[x][y] == Cell.WALL) {
                    rects[x][y].color = Color.CORAL
                } else if (data[x][y] == Cell.SPACE)
                    rects[x][y].color = Color.LIGHT_GRAY
                rects[x][y].draw(batch, 1f)
            }
        }
    }

}