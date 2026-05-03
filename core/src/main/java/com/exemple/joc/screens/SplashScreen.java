package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.exemple.joc.Main;

public class SplashScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private final Texture splashTexture;
    private final Image splashImage;
    private float timer = 0f;
    private boolean transitioned = false;
    private boolean disposed = false;

    public SplashScreen(Main game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.splashTexture = new Texture("images/splash.png");
        this.splashImage = new Image(splashTexture);
        this.splashImage.setPosition(0f, 0f);
        this.splashImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(splashImage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        timer += delta;
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();

        if (!transitioned && timer >= 2f) {
            transitioned = true;
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        splashImage.setSize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
        dispose();
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        stage.dispose();
        splashTexture.dispose();
    }
}
