// Maze generator in Kotlin
// Joe Wingbermuehle
// 2015-07-25

package com.vhundef.game.lib

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.abs


class Maze(var width: Int, var height: Int) {

    private enum class Cell {
        WALL, SPACE, VISITED, FOCUS
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

    private fun generate() {
        if (width % 2 == 0) {
            println("FFS width $width")
        }
        if (height % 2 == 0) {
            println("FFS height $height")
        }
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

        //  data[width - 3][height - 2] = Cell.FOCUS
    }

    fun draw(batch: SpriteBatch) {
        batch.begin()
        val rectSize = Gdx.graphics.width / width
        val rectYOffset = (Gdx.graphics.height - rectSize * height) - (rectSize * height) / 2
        for (y in 0 until height) {
            for (x in 0 until width) {
                rects[x][y].x = (x * rectSize).toFloat()
                rects[x][y].y = rectYOffset + (y * rectSize).toFloat()
                rects[x][y].width = (rectSize).toFloat()
                rects[x][y].height = (rectSize).toFloat()
                if (data[x][y] == Cell.WALL) {
                    rects[x][y].color = Color.LIGHT_GRAY
                } else if (data[x][y] == Cell.SPACE)
                    rects[x][y].color = Color.DARK_GRAY
                else if (data[x][y] == Cell.FOCUS)
                    rects[x][y].color = Color(0f, 1f, 0.5f, 1f)
                else {
                    rects[x][y].color = Color.GREEN
                }
                rects[x][y].draw(batch, 1f)
            }
        }
        batch.end()
    }

    var done = false
    var visitedTiles = 0
    var prevX = 0
    var prevY = 0

    fun checkTile(x: Int, y: Int) {
        println("Got touch at X: $x Y: $y")

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (data[i][(width - 1) - j] == Cell.WALL)
                    continue
                if (x >= rects[i][j].x && x <= rects[i][j].x + rects[i][j].width) {
                    println("X check: Passed")
                    if (y >= rects[i][j].y && y <= rects[i][j].y + rects[i][j].height) {
                        println("Y check: Passed")
                        if (prevX == 0)
                            prevX = i
                        if (prevY == 0)
                            prevY = j
                        if (checkIfCanGo(i, j)) {
                            println("CAN GO")
                            if (data[i][(width - 1) - j] == Cell.SPACE) {
                                data[i][(width - 1) - j] = Cell.FOCUS
                                println("ALL OK")
                                visitedTiles++
                                data[prevX][(width - 1) - prevY] = Cell.VISITED
                                prevX = i
                                prevY = j
                                Gdx.graphics.requestRendering()
                                if (i == 2 && j == height - 2)
                                    done = true
                                return


                            } else if (data[i][(width - 1) - j] == Cell.VISITED || data[i][(width - 1) - j] == Cell.FOCUS) {
                                data[prevX][(width - 1) - prevY] = Cell.VISITED
                                prevX = i
                                prevY = j
                                visitedTiles++
                            }
                        } else {
                            println("NO Go prevX: $prevX X: $x")
                            println("prevY: $prevY Y: $y")
                        }
                    }
                }
            }
        }
    }

    private fun checkIfCanGo(curX: Int, curY: Int): Boolean {
        if (abs(curX - prevX) == 1 && abs(curY - prevY) == 0)
            return true
        if (abs(curX - prevX) == 0 && abs(curY - prevY) == 1)
            return true
        return false
    }
}