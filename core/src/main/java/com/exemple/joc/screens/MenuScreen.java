package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.exemple.joc.Main;

public class MenuScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        batch.begin();
        font.draw(batch, "JOC DELS PERSONATGES", 100, 300);
        font.draw(batch, "Toca (mobil) o ESPAI (PC) per jugar", 100, 250);
        batch.end();

        boolean startPressed = Gdx.input.justTouched();

        if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Android
                && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            startPressed = true;
        }

        if (startPressed) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

    }
}
