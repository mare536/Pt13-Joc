package com.exemple.joc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ScoreManager {
    private final BitmapFont font;
    private final GameConfig config;

    private int score = 0;
    private int nextBonusLifeScore;
    private float scoreTimer = 0f;

    public ScoreManager(GameConfig config) {
        this.config = config;
        this.nextBonusLifeScore = config.bonusLifeScore;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void update(float delta, Player player, AudioManager audioManager) {
        scoreTimer += delta;
        int intervals = (int) (scoreTimer / config.scoreInterval);
        if (intervals > 0) {
            score += intervals * config.scoreStep;
            scoreTimer -= intervals * config.scoreInterval;
        }

        if (config.bonusLifeScore > 0 && score >= nextBonusLifeScore) {
            player.gainBonusLife();
            nextBonusLifeScore += config.bonusLifeScore;
            audioManager.playScore();
        }
    }

    public boolean isNight() {
        if (score < config.nightStartScore) {
            return false;
        }
        int cycleScore = score - config.nightStartScore;
        int cyclePosition = cycleScore % config.nightCyclePeriod;
        return cyclePosition < config.nightDuration;
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
