package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.exemple.joc.Main;

public class MenuScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private final Texture menuTexture;
    private final Image backgroundImage;
    private final Actor startArea;
    private boolean startRequested = false;
    private boolean disposed = false;

    public MenuScreen(Main game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.menuTexture = new Texture("images/menu.png");
        this.backgroundImage = new Image(menuTexture);
        this.backgroundImage.setPosition(0f, 0f);
        this.backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        this.startArea = new Actor();
        this.startArea.setBounds(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.startArea.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startGame();
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                    startGame();
                    return true;
                }
                return false;
            }
        });
        stage.addActor(startArea);
    }

    @Override
    public void show() {
        startRequested = false;
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(startArea);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(delta);
        stage.draw();

        if (startRequested) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        backgroundImage.setSize(width, height);
        startArea.setBounds(0f, 0f, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

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
        menuTexture.dispose();

    }

    private void startGame() {
        if (!startRequested) {
            startRequested = true;
        }
    }
}
