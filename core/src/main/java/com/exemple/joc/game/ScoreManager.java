package com.exemple.joc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ScoreManager {
    private static final float SCORE_INTERVAL = 0.05f;
    private static final int BONUS_LIFE_SCORE = 100;

    private final BitmapFont font;

    private int score = 0;
    private int lastBonusLifeScore = -1;
    private float scoreTimer = 0f;

    public ScoreManager() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void update(float delta, Player player, AudioManager audioManager) {
        scoreTimer += delta;
        if (scoreTimer < SCORE_INTERVAL) {
            return;
        }

        score++;
        scoreTimer = 0f;

        if (score > 0 && score % BONUS_LIFE_SCORE == 0 && score != lastBonusLifeScore) {
            player.gainBonusLife();
            lastBonusLifeScore = score;
            audioManager.playScore();
        }
    }

    public boolean isNight() {
        return score >= 700 && (score % 700) < 200;
    }

    public void drawHud(SpriteBatch batch, int lives, float worldWidth, float worldHeight) {
        font.setColor(isNight() ? Color.WHITE : Color.BLACK);
        font.draw(batch, "Punts: " + score, 20, worldHeight - 20);
        font.draw(batch, "Vides: " + lives, 20, worldHeight - 55);
        font.draw(batch, isNight() ? "NIT" : "DIA", worldWidth - 90, worldHeight - 20);
    }

    public void drawGameOver(SpriteBatch batch, float worldWidth, float worldHeight) {
        font.draw(batch, "GAME OVER", worldWidth / 2f - 90, worldHeight / 2f + 20);
        font.draw(batch, "Prem ENTER per tornar al menu", worldWidth / 2f - 180, worldHeight / 2f - 20);
    }

    public int getScore() {
        return score;
    }

    public void dispose() {
        font.dispose();
    }
}
