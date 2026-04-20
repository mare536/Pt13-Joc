package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.exemple.joc.Main;

public class SplashScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private float timer = 0f;

    public SplashScreen(Main game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        timer += delta;

        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();

        if (timer>=2f){
            game.setScreen(new MenuScreen(game));
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
        stage.dispose();
    }
}
