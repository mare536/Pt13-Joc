package com.exemple.joc.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private final Texture texture;
    private final float y;
    private final float width;
    private final float height;
    private final float speed;
    private final Rectangle bounds;
    private float x;

    public Obstacle(Texture texture, float x, float y, float width, float height, float speed) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        x -= speed * delta;
        bounds.setPosition(x, y);
    }

    public boolean isOffScreen(float margin) {
        return x + width < margin;
    }

    public boolean overlaps(Rectangle other) {
        return bounds.overlaps(other);
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
