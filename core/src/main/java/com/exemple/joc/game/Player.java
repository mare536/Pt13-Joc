package com.exemple.joc.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private static final float PLAYER_X = 90f;
    private static final float GROUND_Y = 90f;
    private static final float PLAYER_WIDTH = 64f;
    private static final float PLAYER_HEIGHT = 72f;
    private static final float DUCK_HEIGHT = 48f;

    private final Rectangle bounds = new Rectangle();

    private final int maxJumps;
    private final float gravity;
    private final float jumpVelocity;
    private final float fastFallVelocity;
    private final float fastFallGravityMultiplier;

    private float y = GROUND_Y;
    private float velocityY = 0f;
    private boolean ducking = false;
    private int lives;
    private int remainingJumps;

    public Player(GameConfig config) {
        this.lives = config.startingLives;
        this.maxJumps = config.allowDoubleJump ? 2 : 1;
        this.remainingJumps = maxJumps;
        this.gravity = config.gravity;
        this.jumpVelocity = config.jumpVelocity;
        this.fastFallVelocity = config.fastFallVelocity;
        this.fastFallGravityMultiplier = config.fastFallGravityMultiplier;
        updateBounds();
    }

    public void jump(AudioManager audioManager) {
        if (remainingJumps <= 0) {
            return;
        }

        velocityY = jumpVelocity;
        remainingJumps--;
        audioManager.playJump();
    }

    public void update(boolean downPressed, float delta) {
        ducking = downPressed && isOnGround();

        if (downPressed && !isOnGround()) {
            velocityY = fastFallVelocity;
        }

        float gravityMultiplier = (downPressed && !isOnGround()) ? fastFallGravityMultiplier : 1f;
        velocityY -= gravity * gravityMultiplier * delta;
        y += velocityY * delta;

        if (y < GROUND_Y) {
            y = GROUND_Y;
            velocityY = 0f;
            remainingJumps = maxJumps;
        }

        updateBounds();
    }

    public void gainBonusLife() {
        if (lives < GameConfig.MAX_LIVES) {
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
