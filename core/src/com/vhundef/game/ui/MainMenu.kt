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


class MainMenu(private val game: Game) : Screen {

    private val stage: Stage = Stage(ScreenViewport())

    init {

        val title = Label("Нету тут пока что названия", MyGame.gameSkin)
        title.setFontScale(3f)
        title.setAlignment(Align.center)
        title.y = (Gdx.graphics.height * 2 / 3).toFloat()
        title.width = Gdx.graphics.width.toFloat()
        title.color = Color.BLACK
        stage.addActor(title)

        val playButton = TextButton("New game", MyGame.gameSkin)

        playButton.width = Gdx.graphics.width.toFloat()
        playButton.height = Gdx.graphics.height / 9f
        playButton.color = Color.LIME
        playButton.label.setFontScale(playButton.label.width / 30f)
        playButton.setPosition(0f, Gdx.graphics.height / 2 - playButton.height / 2)
        playButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = GameScreen(game, 1)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(playButton)

        val optionsButton = TextButton("Continue", MyGame.gameSkin)
        optionsButton.width = Gdx.graphics.width.toFloat()
        optionsButton.height = Gdx.graphics.height / 9f
        optionsButton.color = Color.LIME
        optionsButton.label.setFontScale(playButton.label.fontScaleX)
        optionsButton.setPosition(0f, Gdx.graphics.height / 2 - optionsButton.height * 1.6f)
        optionsButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                //   game.screen = OptionScreen(game)
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(optionsButton)

        val quitButton = TextButton("Quit", MyGame.gameSkin)
        quitButton.width = Gdx.graphics.width.toFloat()
        quitButton.height = Gdx.graphics.height / 9f
        quitButton.color = Color.LIME
        quitButton.label.setFontScale(optionsButton.label.fontScaleX)
        quitButton.setPosition(0f, Gdx.graphics.height / 2 - optionsButton.height * 2.7f)
        quitButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                Gdx.app.exit()
            }

            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(quitButton)

        val thx = Label("Спасибо за тест!\nV0.3", MyGame.gameSkin)
        thx.setFontScale(3f)
        thx.setAlignment(Align.center)
        thx.y = (100f).toFloat()
        thx.width = Gdx.graphics.width.toFloat()
        thx.color = Color.BLACK
        stage.addActor(thx)
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