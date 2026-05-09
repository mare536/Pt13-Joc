package com.exemple.joc.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private static final int MAX_LIVES = 5;

    private static final float PLAYER_X = 90f;
    private static final float GROUND_Y = 90f;
    private static final float PLAYER_WIDTH = 64f;
    private static final float PLAYER_HEIGHT = 72f;
    private static final float DUCK_HEIGHT = 48f;
    private static final float GRAVITY = 1800f;
    private static final float JUMP_VELOCITY = 780f;
    private static final float FAST_FALL_VELOCITY = -1800f;
    private static final float FAST_FALL_GRAVITY_MULT = 3.0f;

    private final Rectangle bounds = new Rectangle();

    private float y = GROUND_Y;
    private float velocityY = 0f;
    private boolean ducking = false;
    private int lives = 3;

    public Player() {
        updateBounds();
    }

    public void jump(AudioManager audioManager) {
        if (!isOnGround()) {
            return;
        }

        velocityY = JUMP_VELOCITY;
        audioManager.playJump();
    }

    public void update(boolean downPressed, float delta) {
        ducking = downPressed && isOnGround();

        if (downPressed && !isOnGround()) {
            velocityY = FAST_FALL_VELOCITY;
        }

        float gravityMultiplier = (downPressed && !isOnGround()) ? FAST_FALL_GRAVITY_MULT : 1f;
        velocityY -= GRAVITY * gravityMultiplier * delta;
        y += velocityY * delta;

        if (y < GROUND_Y) {
            y = GROUND_Y;
            velocityY = 0f;
        }

        updateBounds();
    }

    public void gainBonusLife() {
        if (lives < MAX_LIVES) {
            lives++;
        }
    }

    public void loseLife() {
        lives--;
    }

    public boolean hasLivesLeft() {
        return lives > 0;
    }

    public int getLives() {
        return lives;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private boolean isOnGround() {
        return y <= GROUND_Y + 0.01f;
    }

    public void draw(SpriteBatch batch, Texture texture) {
        batch.draw(texture, PLAYER_X, y, PLAYER_WIDTH, getDrawHeight());
    }

    private void updateBounds() {
        bounds.set(PLAYER_X, y, PLAYER_WIDTH, getDrawHeight());
    }

    private float getDrawHeight() {
        return ducking ? DUCK_HEIGHT : PLAYER_HEIGHT;
    }
}
