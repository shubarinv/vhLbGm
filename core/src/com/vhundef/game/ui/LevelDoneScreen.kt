package com.vhundef.game.ui

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.vhundef.game.MyGame
import com.vhundef.game.MyGame.level

class LevelDoneScreen(private val game: Game, private val visitedTiles: Int) : Screen {
    private val stage: Stage = Stage(ScreenViewport())

    init {
        var currLevel = MyGame.level
        val title = Label("LEVEL $currLevel DONE\nused $visitedTiles tiles", MyGame.gameSkin)
        title.setFontScale(8f)
        title.setAlignment(Align.center)
        title.y = (Gdx.graphics.height * 2 / 3).toFloat()
        title.width = Gdx.graphics.width.toFloat()
        title.color = Color.BLACK
        stage.addActor(title)
        MyGame.level += 1

        val playButton = TextButton("Next", MyGame.gameSkin)

        playButton.width = Gdx.graphics.width.toFloat()
        playButton.height = Gdx.graphics.height / 9f
        playButton.color = Color.LIME
        playButton.label.setFontScale(playButton.height / 25f)
        playButton.setPosition(0f, Gdx.graphics.height / 2 - playButton.height / 2)
        playButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = GameScreen(game, level)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(playButton)

        val quitButton = TextButton("Quit", MyGame.gameSkin)
        quitButton.width = Gdx.graphics.width.toFloat()
        quitButton.height = Gdx.graphics.height / 9f
        quitButton.color = Color.LIME
        quitButton.label.setFontScale(quitButton.height / 25f)
        quitButton.setPosition(0f, Gdx.graphics.height / 2 - quitButton.height * 2.7f)
        quitButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                Gdx.app.exit()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(quitButton)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
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
}