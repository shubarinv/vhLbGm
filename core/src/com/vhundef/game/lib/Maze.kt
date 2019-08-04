// Maze generator in Kotlin
// Joe Wingbermuehle
// 2015-07-25

package com.vhundef.game.lib

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.vhundef.game.MyGame
import com.vhundef.game.ui.GameScreen
import kotlin.math.abs
import kotlin.math.min


class Maze(var width: Int, var height: Int) {
    private enum class Cell {
        WALL, SPACE, VISITED, FOCUS, VOID
    }

    private var data = Array(width) { i ->
        Array(height) { j -> Cell.WALL }
    }

    private val rects = Array(width) { i ->
        Array<Rect>(height) { j -> Rect(1f, 1f, 1f, 1f, Color.WHITE) }
    }

    private val rand = java.util.Random()

    var playerRectangle = Rect((Gdx.graphics.width - (Gdx.graphics.width / width) * 3).toFloat() - 10, (Gdx.graphics.height - Gdx.graphics.width / width * height) - (Gdx.graphics.width / width * height) / 2.toFloat() + (Gdx.graphics.width / width) * (height - 2), 1f, 1f, Color(1f, 0.5f, 0.5f, 1f))

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
        data[1][0] = Cell.WALL
        data[3][0] = Cell.WALL

        data[2][2] = Cell.SPACE
        carve(2, 2)
        for (x in 0 until width) {
            data[x][0] = Cell.VOID
            data[x][height - 1] = Cell.VOID
        }
        for (y in 0 until height) {
            data[0][y] = Cell.VOID
            data[width - 1][y] = Cell.VOID
        }
        data[2][1] = Cell.SPACE
        data[width - 3][height - 2] = Cell.SPACE
        playerRectangle.width = (Gdx.graphics.width / width).toFloat() / 2
        playerRectangle.height = playerRectangle.width
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
                when {
                    data[x][y] == Cell.WALL -> rects[x][y].color = Color.LIGHT_GRAY
                    data[x][y] == Cell.SPACE -> rects[x][y].color = Color.DARK_GRAY
                    data[x][y] == Cell.VOID -> rects[x][y].color = Color.WHITE
                    data[x][y] == Cell.FOCUS -> rects[x][y].color = Color(1f, 0.5f, 0.5f, 1f)
                    else -> rects[x][y].color = Color.GREEN
                }
                rects[x][y].draw(batch, 1f)
            }
        }
        playerRectangle.draw(batch, 1f)
        batch.end()
    }

    var done = false
    var visitedTiles = 0

    fun drawEnd(stage: Stage) {
        val labelStart = Label("Start", MyGame.gameSkin)
        labelStart.setFontScale(3f)
        labelStart.setAlignment(Align.center)
        labelStart.y = ((Gdx.graphics.height - (Gdx.graphics.width / width * height) / 2).toFloat())
        labelStart.x = ((Gdx.graphics.width - (Gdx.graphics.width / width) * 3).toFloat())
        labelStart.color = Color.BLACK
        stage.addActor(labelStart)

        val labelEnd = Label("End", MyGame.gameSkin)
        labelEnd.setFontScale(3f)
        labelEnd.setAlignment(Align.center)
        labelEnd.y = ((Gdx.graphics.height - Gdx.graphics.width / width * height) - (Gdx.graphics.width / width * height) / 2).toFloat() - Gdx.graphics.width / width
        labelEnd.x = ((Gdx.graphics.width / width) * 2).toFloat() + (Gdx.graphics.width / width) / 4
        labelEnd.color = Color.BLACK
        stage.addActor(labelEnd)
    }

    var touchX = 0
    var touchY = 0
    var newTouch = false
    private var prevX = width - 3
    private var prevY = 9
    private var dt = min(Gdx.graphics.deltaTime, 1 / 60f)
    fun checkTileN(x: Int, y: Int, gameScreen: GameScreen) {
        var flag = false
        if (newTouch) {
            //      println("start pos X: $prevX Y:$prevY =" + data[prevX][prevY])
            newTouch = false
            //       println("Check tile Called")
            if (abs(touchX - x) > abs(touchY - y)) {
                if (touchX > x) {
                    //             println("Left")
                    while (!newTouch && !flag) {
                        for (i in 1 until width - 1) {
                            if (playerRectangle.overlaps(rects[i][prevY])) {
                                prevX = i
                                //   println("TIle[" + i + "][" + prevY + "] = " + data[i][prevY])
                                if (data[i][prevY] == Cell.WALL || data[i][prevY] == Cell.VOID) {
                                    flag = true
                                    prevX = i + 1
                                    playerRectangle.x += 0.02f * dt
                                    //      println("Hit " + data[i][prevY])
                                    //      println("Pos now X: $prevX Y:$prevY =" + data[prevX][prevY])
                                    break
                                }
                                break
                            }
                            if (prevX == 2 && prevY <= 2) {
                                done = true
                                return
                            }
                            dt = min(Gdx.graphics.deltaTime, 1 / 60f)
                            playerRectangle.x -= 0.015f * dt
                            Gdx.graphics.requestRendering()
                        }
                    }
                    //             println("Break")
                }
                if (touchX < x) {
                    //           println("Right")
                    while (!newTouch && !flag) {
                        for (i in 1 until width - 2) {
                            if (playerRectangle.overlaps(rects[i][prevY])) {
                                prevX = i
                                // println("TIle[" + i + "][" + prevY + "] = " + data[i][prevY])
                                if (data[i + 1][prevY] == Cell.WALL || data[i + 1][prevY] == Cell.VOID) {
                                    flag = true
                                    prevX = i
                                    playerRectangle.x -= 0.02f * dt
                                    //     println("Hit " + data[i + 1][prevY])
                                    //     println("Pos now X: $prevX Y:$prevY =" + data[prevX][prevY])
                                    break
                                }
                                break
                            }
                            if (prevX == 2 && prevY <= 2) {
                                done = true
                                return
                            }
                            dt = min(Gdx.graphics.deltaTime, 1 / 60f)
                            playerRectangle.x += 0.015f * dt
                            Gdx.graphics.requestRendering()
                        }
                    }
                    //          println("Break")
                }
            } else {
                if (touchY < y) {
                    //          println("Up")
                    while (!newTouch && !flag) {
                        for (i in 1 until height - 1) {
                            if (playerRectangle.overlaps(rects[prevX][i])) {
                                prevY = i
                                //   println("TIle[" + prevX + "][" + i + "] = " + data[prevX][i])
                                if (data[prevX][i + 1] == Cell.WALL || data[prevX][i + 1] == Cell.VOID) {
                                    flag = true
                                    playerRectangle.y -= 0.02f * dt
                                    //    println("Hit " + data[prevX][i + 1])
                                    //     println("Pos now X: $prevX Y:$prevY =" + data[prevX][prevY])
                                    break
                                }
                                break
                            }
                            if (prevX == 2 && prevY <= 2) {
                                done = true
                                return
                            }
                            dt = min(Gdx.graphics.deltaTime, 1 / 60f)
                            playerRectangle.y += 0.015f * dt
                            Gdx.graphics.requestRendering()
                        }
                    }
                    //         println("Break")
                }
                if (touchY > y) {
                    //            println("Down")
                    while (!newTouch && !flag) {
                        for (i in 1 until height - 1) {
                            if (playerRectangle.overlaps(rects[prevX][i])) {
                                prevY = i
                                // println("TIle[" + prevX + "][" + i + "] = " + data[prevX][i])
                                if (data[prevX][i] == Cell.WALL || data[prevX][i] == Cell.VOID) {
                                    flag = true
                                    prevY = i + 1
                                    playerRectangle.y += 0.02f * dt
                                    //    println("Hit " + data[prevX][i])
                                    //    println("Pos now X: $prevX Y:$prevY =" + data[prevX][prevY])
                                    break
                                }
                                break
                            }
                            if (prevX == 2 && prevY <= 2) {
                                done = true
                                return
                            }
                            dt = min(Gdx.graphics.deltaTime, 1 / 60f)
                            playerRectangle.y -= 0.015f * dt
                            Gdx.graphics.requestRendering()

                        }
                    }
                    //             println("Break")
                }
            }
        }
    }
    private fun checkIfCanGo(curX: Int, curY: Int): Boolean {
        if (abs(curX - prevX) == 1 && abs(curY - prevY) == 0)
            return true
        if (abs(curX - prevX) == 0 && abs(curY - prevY) == 1)
            return true
        return true
    }
}
