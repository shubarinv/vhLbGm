package com.vhundef.game.ui

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.vhundef.game.lib.Maze

class GameScreen(game: Game) : Screen, InputProcessor {
    internal inner class TouchInfo {
        var touchX = 0f
        var touchY = 0f
        var touched = false
    }

    private var touch = TouchInfo()
    private val stage: Stage
    var maze = Maze(4, 4)
    private var batch: SpriteBatch? = null

    init {
        batch = SpriteBatch()
        stage = Stage(ScreenViewport() as Viewport?)
        maze = Maze(15, 15)
        Gdx.input.inputProcessor = this
        Gdx.graphics.isContinuousRendering = false
    }

    override fun show() {
        //   Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
        maze.draw(batch!!)
        println("REDRAW")
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        stage.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //maze.checkTile(screenX, screenY)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        maze.checkTile(screenX, screenY)
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        // TODO Auto-generated method stub
        return false
    }
}
