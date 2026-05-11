package com.exemple.joc.game;

import com.badlogic.gdx.math.MathUtils;

public class GameConfig {
    public static final int MAX_LIVES = 5;

    public final float baseSpeed;
    public final float speedIncreasePerScore;
    public final float spawnIntervalStart;
    public final float spawnIntervalMin;
    public final float spawnIntervalScoreFactor;
    public final float spawnSkipChance;
    public final float birdSpawnChance;
    public final float birdSpeedBonus;
    public final float obstacleScaleMin;
    public final float obstacleScaleMax;
    public final float hitCooldownSeconds;
    public final int startingLives;
    public final boolean allowDoubleJump;
    public final int bonusLifeScore;
    public final float scoreInterval;
    public final int scoreStep;
    public final int nightStartScore;
    public final int nightCyclePeriod;
    public final int nightDuration;
    public final float gravity;
    public final float jumpVelocity;
    public final float fastFallVelocity;
    public final float fastFallGravityMultiplier;

    private GameConfig(float baseSpeed, float speedIncreasePerScore, float spawnIntervalStart,
                       float spawnIntervalMin, float spawnIntervalScoreFactor, float spawnSkipChance,
                       float birdSpawnChance, float birdSpeedBonus, float obstacleScaleMin,
                       float obstacleScaleMax, float hitCooldownSeconds, int startingLives,
                       boolean allowDoubleJump, int bonusLifeScore, float scoreInterval,
                       int scoreStep, int nightStartScore, int nightCyclePeriod, int nightDuration,
                       float gravity, float jumpVelocity, float fastFallVelocity,
                       float fastFallGravityMultiplier) {
        this.baseSpeed = Math.max(0f, baseSpeed);
        this.speedIncreasePerScore = Math.max(0f, speedIncreasePerScore);
        this.spawnIntervalStart = Math.max(0.1f, spawnIntervalStart);
        this.spawnIntervalMin = Math.max(0.1f, Math.min(spawnIntervalMin, this.spawnIntervalStart));
        this.spawnIntervalScoreFactor = Math.max(100f, spawnIntervalScoreFactor);
        this.spawnSkipChance = MathUtils.clamp(spawnSkipChance, 0f, 1f);
        this.birdSpawnChance = MathUtils.clamp(birdSpawnChance, 0f, 1f);
        this.birdSpeedBonus = Math.max(0f, birdSpeedBonus);
        float minScale = MathUtils.clamp(obstacleScaleMin, 0.6f, 2.0f);
        float maxScale = MathUtils.clamp(obstacleScaleMax, 0.6f, 2.0f);
        this.obstacleScaleMin = Math.min(minScale, maxScale);
        this.obstacleScaleMax = Math.max(minScale, maxScale);
        this.hitCooldownSeconds = Math.max(0.1f, hitCooldownSeconds);
        this.startingLives = Math.max(1, Math.min(startingLives, MAX_LIVES));
        this.allowDoubleJump = allowDoubleJump;
        this.bonusLifeScore = Math.max(0, bonusLifeScore);
        this.scoreInterval = Math.max(0.01f, scoreInterval);
        this.scoreStep = Math.max(1, scoreStep);
        this.nightStartScore = Math.max(0, nightStartScore);
        this.nightCyclePeriod = Math.max(1, nightCyclePeriod);
        this.nightDuration = Math.min(Math.max(0, nightDuration), this.nightCyclePeriod);
        this.gravity = Math.max(100f, gravity);
        this.jumpVelocity = Math.max(100f, jumpVelocity);
        this.fastFallVelocity = Math.min(-100f, fastFallVelocity);
        this.fastFallGravityMultiplier = Math.max(1f, fastFallGravityMultiplier);
    }

    public static GameConfig normal() {
        return new GameConfig(
            260f,
            0.08f,
            1.6f,
            0.9f,
            1500f,
            0.2f,
            0.25f,
            50f,
            1.0f,
            1.0f,
            0.7f,
            3,
            false,
            100,
            0.05f,
            1,
            700,
            700,
            200,
            1800f,
            780f,
            -1800f,
            3.0f
        );
    }

    public static GameConfig easy() {
        return new GameConfig(
            220f,
            0.06f,
            1.9f,
            1.2f,
            1800f,
            0.3f,
            0.15f,
            40f,
            0.9f,
            1.0f,
            0.9f,
            5,
            false,
            80,
            0.06f,
            1,
            900,
            800,
            180,
            1700f,
            760f,
            -1600f,
            2.5f
        );
    }

    public static GameConfig hard() {
        return new GameConfig(
            310f,
            0.1f,
            1.3f,
            0.7f,
            1200f,
            0.05f,
            0.35f,
            70f,
            1.0f,
            1.2f,
            0.6f,
            2,
            false,
            140,
            0.05f,
            1,
            600,
            650,
            230,
            1900f,
            780f,
            -2000f,
            3.2f
        );
    }

    public static GameConfig doubleJump() {
        return new GameConfig(
            260f,
            0.08f,
            1.6f,
            0.9f,
            1500f,
            0.2f,
            0.25f,
            50f,
            1.0f,
            1.0f,
            0.7f,
            3,
            true,
            100,
            0.05f,
            1,
            700,
            700,
            200,
            1800f,
            780f,
            -1800f,
            3.0f
        );
    }

    public static GameConfig denseObstacles() {
        return new GameConfig(
            280f,
            0.09f,
            1.2f,
            0.6f,
            1300f,
            0.0f,
            0.4f,
            60f,
            0.9f,
            1.2f,
            0.5f,
            3,
            false,
            120,
            0.05f,
            1,
            650,
            600,
            220,
            1850f,
            780f,
            -1900f,
            3.0f
        );
    }

    public static GameConfig floaty() {
        return new GameConfig(
            240f,
            0.07f,
            1.7f,
            1.0f,
            1700f,
            0.25f,
            0.2f,
            40f,
            0.95f,
            1.05f,
            0.8f,
            4,
            true,
            90,
            0.06f,
            1,
            800,
            750,
            200,
            1200f,
            700f,
            -1400f,
            2.0f
        );
    }

    public static GameConfig endlessNight() {
        return new GameConfig(
            260f,
            0.08f,
            1.5f,
            0.9f,
            1500f,
            0.2f,
            0.3f,
            50f,
            1.0f,
            1.1f,
            0.7f,
            3,
            false,
            100,
            0.05f,
            1,
            0,
            400,
            400,
            1800f,
            780f,
            -1800f,
            3.0f
        );
    }

    public static GameConfig rapidScore() {
        return new GameConfig(
            255f,
            0.08f,
            1.6f,
            0.9f,
            1500f,
            0.2f,
            0.25f,
            50f,
            1.0f,
            1.0f,
            0.7f,
            3,
            false,
            100,
            0.035f,
            2,
            700,
            700,
            200,
            1800f,
            780f,
            -1800f,
            3.0f
        );
    }

    /**
     * Preset intended for customization: adjust these values according to your requirements.
     */
    public static GameConfig custom() {
        return new GameConfig(
            250f,
            0.08f,
            1.6f,
            0.9f,
            1500f,
            0.2f,
            0.25f,
            50f,
            1.0f,
            1.0f,
            0.7f,
            3,
            false,
            100,
            0.05f,
            1,
            700,
            700,
            200,
            1800f,
            780f,
            -1800f,
            3.0f
        );
    }

}
