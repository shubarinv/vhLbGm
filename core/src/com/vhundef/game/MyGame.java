package com.vhundef.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.vhundef.game.ui.MainMenu;

public class MyGame extends Game {
    static public Skin gameSkin;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameSkin = new Skin(Gdx.files.internal("data/level-plane/skin/level-plane-ui.json"));
        this.setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
