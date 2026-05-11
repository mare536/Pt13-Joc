package com.exemple.joc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;

public class ScoreManager {
    private final BitmapFont font;
    private final GameConfig config;

    private int score = 0;
    private int nextBonusLifeScore;
    private int nextShieldScore;
    private float scoreTimer = 0f;

    public ScoreManager(GameConfig config) {
        this.config = config;
        this.nextBonusLifeScore = config.bonusLifeScore;
        this.nextShieldScore = config.shieldScoreEvery;
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
            scoreTimer = MathUtils.clamp(
                scoreTimer - (intervals * config.scoreInterval),
                0f,
                config.scoreInterval
            );
        }

        applyThresholds(player, audioManager);
    }

    public void addBonus(int amount, Player player, AudioManager audioManager) {
        if (amount <= 0) {
            return;
        }
        score += amount;
        applyThresholds(player, audioManager);
    }

    private void applyThresholds(Player player, AudioManager audioManager) {
        if (config.bonusLifeScore > 0) {
            while (score >= nextBonusLifeScore) {
                player.gainBonusLife();
                nextBonusLifeScore += config.bonusLifeScore;
                audioManager.playScore();
            }
        }

        if (config.enableShield && config.shieldScoreEvery > 0) {
            while (score >= nextShieldScore) {
                player.grantShield();
                nextShieldScore += config.shieldScoreEvery;
                audioManager.playScore();
            }
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

    public void drawHud(SpriteBatch batch, int lives, int shields, float ghostTimer,
                        float ghostCooldown, float worldWidth, float worldHeight) {
        font.setColor(isNight() ? Color.WHITE : Color.BLACK);
        font.draw(batch, "Punts: " + score, 20, worldHeight - 20);
        font.draw(batch, "Vides: " + lives, 20, worldHeight - 55);
        font.draw(batch, isNight() ? "NIT" : "DIA", worldWidth - 90, worldHeight - 20);
        if (config.enableShield) {
            font.draw(batch, "Escut: " + shields, 20, worldHeight - 90);
        }
        if (config.enableGhostMode) {
            String ghostLabel = ghostTimer > 0f
                ? "FANTASMA"
                : "RECARGA " + MathUtils.ceil(ghostCooldown) + "s";
            font.draw(batch, ghostLabel, worldWidth - 170, worldHeight - 55);
        }
    }

    public void drawPause(SpriteBatch batch, float worldWidth, float worldHeight) {
        font.setColor(Color.WHITE);
        font.draw(batch, "PAUSA (P/ESC)", worldWidth / 2f - 90f, worldHeight / 2f + 10f);
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
