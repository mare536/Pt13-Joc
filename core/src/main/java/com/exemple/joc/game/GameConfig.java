package com.exemple.joc.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * Centralized gameplay tuning values for speed, spawning, scoring, and physics.
 * Presets are simple data-only variants; edit {@link #custom()} to create your own mode.
 * Chance fields are 0..1, interval fields are positive, and scale fields are 0.6..2.0.
 */
public class GameConfig {
    public static final int MAX_LIVES = 5;

    /** Base obstacle speed in pixels per second. */
    public final float baseSpeed;
    /** Extra speed added per score point. */
    public final float speedIncreasePerScore;
    /** Starting seconds between spawns. */
    public final float spawnIntervalStart;
    /** Minimum seconds between spawns. */
    public final float spawnIntervalMin;
    /** Score divisor used to reduce spawn interval as score grows. */
    public final float spawnIntervalScoreFactor;
    /** Chance to skip an obstacle spawn (0 = always spawn). */
    public final float spawnSkipChance;
    /** Chance that a spawn is a bird. */
    public final float birdSpawnChance;
    /** Extra speed applied to bird obstacles. */
    public final float birdSpeedBonus;
    /** Minimum obstacle scale multiplier. */
    public final float obstacleScaleMin;
    /** Maximum obstacle scale multiplier. */
    public final float obstacleScaleMax;
    /** Seconds of invulnerability after a hit. */
    public final float hitCooldownSeconds;
    /** Initial lives given to the player. */
    public final int startingLives;
    /** Whether double jump is enabled. */
    public final boolean allowDoubleJump;
    /** Score needed for a bonus life (0 disables). */
    public final int bonusLifeScore;
    /** Seconds per score tick. */
    public final float scoreInterval;
    /** Points added each score tick. */
    public final int scoreStep;
    /** Score at which night cycles start. */
    public final int nightStartScore;
    /** Score length of a full day/night cycle. */
    public final int nightCyclePeriod;
    /** Score duration of the night portion of the cycle. */
    public final int nightDuration;
    /** Gravity applied to the player. */
    public final float gravity;
    /** Vertical velocity applied when jumping. */
    public final float jumpVelocity;
    /** Vertical velocity applied for fast fall. */
    public final float fastFallVelocity;
    /** Gravity multiplier while fast falling. */
    public final float fastFallGravityMultiplier;
    /** Enables pause toggle (P/ESC on desktop, two fingers on Android). */
    public final boolean enablePause;
    /** Enables shield charges earned by score. */
    public final boolean enableShield;
    /** Score needed for each shield charge. */
    public final int shieldScoreEvery;
    /** Maximum number of shield charges. */
    public final int shieldMaxCharges;
    /** Enables stomp (destroy obstacle when landing on top). */
    public final boolean enableStomp;
    /** Upward velocity applied on stomp bounce. */
    public final float stompBounceVelocity;
    /** Minimum height ratio needed to count as a stomp (0..1). */
    public final float stompMinHeightRatio;
    /** Extra score granted for a stomp. */
    public final int stompScoreBonus;
    /** Enables ghost mode (temporary no-collision ability). */
    public final boolean enableGhostMode;
    /** Ghost mode duration in seconds. */
    public final float ghostModeDurationSeconds;
    /** Cooldown before ghost mode can be used again. */
    public final float ghostModeCooldownSeconds;

    private GameConfig(Builder builder) {
        this.baseSpeed = Math.max(0f, builder.baseSpeed);
        this.speedIncreasePerScore = Math.max(0f, builder.speedIncreasePerScore);
        this.spawnIntervalStart = Math.max(0.1f, builder.spawnIntervalStart);
        this.spawnIntervalMin = Math.max(0.1f, Math.min(builder.spawnIntervalMin, this.spawnIntervalStart));
        this.spawnIntervalScoreFactor = Math.max(100f, builder.spawnIntervalScoreFactor);
        this.spawnSkipChance = MathUtils.clamp(builder.spawnSkipChance, 0f, 1f);
        this.birdSpawnChance = MathUtils.clamp(builder.birdSpawnChance, 0f, 1f);
        this.birdSpeedBonus = Math.max(0f, builder.birdSpeedBonus);
        float minScale = MathUtils.clamp(builder.obstacleScaleMin, 0.6f, 2.0f);
        float maxScale = MathUtils.clamp(builder.obstacleScaleMax, 0.6f, 2.0f);
        this.obstacleScaleMin = Math.min(minScale, maxScale);
        this.obstacleScaleMax = Math.max(minScale, maxScale);
        this.hitCooldownSeconds = Math.max(0.1f, builder.hitCooldownSeconds);
        this.startingLives = Math.max(1, Math.min(builder.startingLives, MAX_LIVES));
        this.allowDoubleJump = builder.allowDoubleJump;
        this.bonusLifeScore = Math.max(0, builder.bonusLifeScore);
        this.scoreInterval = Math.max(0.01f, builder.scoreInterval);
        this.scoreStep = Math.max(1, builder.scoreStep);
        this.nightStartScore = Math.max(0, builder.nightStartScore);
        this.nightCyclePeriod = Math.max(1, builder.nightCyclePeriod);
        this.nightDuration = Math.min(Math.max(0, builder.nightDuration), this.nightCyclePeriod);
        this.gravity = Math.max(100f, builder.gravity);
        this.jumpVelocity = Math.max(100f, builder.jumpVelocity);
        this.fastFallVelocity = Math.min(-100f, builder.fastFallVelocity);
        this.fastFallGravityMultiplier = Math.max(1f, builder.fastFallGravityMultiplier);
        this.enablePause = builder.enablePause;
        this.enableShield = builder.enableShield;
        this.shieldScoreEvery = Math.max(0, builder.shieldScoreEvery);
        this.shieldMaxCharges = Math.max(0, builder.shieldMaxCharges);
        this.enableStomp = builder.enableStomp;
        this.stompBounceVelocity = Math.max(0f, builder.stompBounceVelocity);
        this.stompMinHeightRatio = MathUtils.clamp(builder.stompMinHeightRatio, 0f, 1f);
        this.stompScoreBonus = Math.max(0, builder.stompScoreBonus);
        this.enableGhostMode = builder.enableGhostMode;
        this.ghostModeDurationSeconds = Math.max(0.1f, builder.ghostModeDurationSeconds);
        this.ghostModeCooldownSeconds = Math.max(0f, builder.ghostModeCooldownSeconds);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static GameConfig normal() {
        return builder().build();
    }

    public static GameConfig easy() {
        return builder()
            .baseSpeed(220f)
            .speedIncreasePerScore(0.06f)
            .spawnIntervalStart(1.9f)
            .spawnIntervalMin(1.2f)
            .spawnIntervalScoreFactor(1800f)
            .spawnSkipChance(0.3f)
            .birdSpawnChance(0.15f)
            .birdSpeedBonus(40f)
            .obstacleScaleMin(0.9f)
            .obstacleScaleMax(1.0f)
            .hitCooldownSeconds(0.9f)
            .startingLives(5)
            .bonusLifeScore(80)
            .scoreInterval(0.06f)
            .nightStartScore(900)
            .nightCyclePeriod(800)
            .nightDuration(180)
            .gravity(1700f)
            .jumpVelocity(760f)
            .fastFallVelocity(-1600f)
            .fastFallGravityMultiplier(2.5f)
            .build();
    }

    public static GameConfig hard() {
        return builder()
            .baseSpeed(310f)
            .speedIncreasePerScore(0.1f)
            .spawnIntervalStart(1.3f)
            .spawnIntervalMin(0.7f)
            .spawnIntervalScoreFactor(1200f)
            .spawnSkipChance(0.05f)
            .birdSpawnChance(0.35f)
            .birdSpeedBonus(70f)
            .obstacleScaleMax(1.2f)
            .hitCooldownSeconds(0.6f)
            .startingLives(2)
            .bonusLifeScore(140)
            .nightStartScore(600)
            .nightCyclePeriod(650)
            .nightDuration(230)
            .gravity(1900f)
            .fastFallVelocity(-2000f)
            .fastFallGravityMultiplier(3.2f)
            .build();
    }

    public static GameConfig doubleJump() {
        return builder()
            .allowDoubleJump(true)
            .build();
    }

    public static GameConfig featurePack() {
        return builder()
            .enableShield(true)
            .enableStomp(true)
            .enableGhostMode(true)
            .build();
    }

    public static GameConfig denseObstacles() {
        return builder()
            .baseSpeed(280f)
            .speedIncreasePerScore(0.09f)
            .spawnIntervalStart(1.2f)
            .spawnIntervalMin(0.6f)
            .spawnIntervalScoreFactor(1300f)
            .spawnSkipChance(0.0f)
            .birdSpawnChance(0.4f)
            .birdSpeedBonus(60f)
            .obstacleScaleMin(0.9f)
            .obstacleScaleMax(1.2f)
            .hitCooldownSeconds(0.5f)
            .bonusLifeScore(120)
            .nightStartScore(650)
            .nightCyclePeriod(600)
            .nightDuration(220)
            .gravity(1850f)
            .fastFallVelocity(-1900f)
            .build();
    }

    public static GameConfig floaty() {
        return builder()
            .baseSpeed(240f)
            .speedIncreasePerScore(0.07f)
            .spawnIntervalStart(1.7f)
            .spawnIntervalMin(1.0f)
            .spawnIntervalScoreFactor(1700f)
            .spawnSkipChance(0.25f)
            .birdSpawnChance(0.2f)
            .birdSpeedBonus(40f)
            .obstacleScaleMin(0.95f)
            .obstacleScaleMax(1.05f)
            .hitCooldownSeconds(0.8f)
            .startingLives(4)
            .allowDoubleJump(true)
            .bonusLifeScore(90)
            .scoreInterval(0.06f)
            .nightStartScore(800)
            .nightCyclePeriod(750)
            .nightDuration(200)
            .gravity(1200f)
            .jumpVelocity(700f)
            .fastFallVelocity(-1400f)
            .fastFallGravityMultiplier(2.0f)
            .build();
    }

    public static GameConfig endlessNight() {
        return builder()
            .spawnIntervalStart(1.5f)
            .birdSpawnChance(0.3f)
            .obstacleScaleMax(1.1f)
            .nightStartScore(0)
            .nightCyclePeriod(400)
            .nightDuration(400)
            .build();
    }

    public static GameConfig rapidScore() {
        return builder()
            .baseSpeed(255f)
            .scoreInterval(0.035f)
            .scoreStep(2)
            .build();
    }

    /**
     * Preset intended for customization: adjust these values according to your requirements.
     */
    public static GameConfig custom() {
        return builder()
            .baseSpeed(250f)
            .speedIncreasePerScore(0.08f)
            .spawnIntervalStart(1.6f)
            .spawnIntervalMin(0.9f)
            .spawnIntervalScoreFactor(1500f)
            .spawnSkipChance(0.2f)
            .birdSpawnChance(0.25f)
            .birdSpeedBonus(50f)
            .obstacleScaleMin(1.0f)
            .obstacleScaleMax(1.0f)
            .hitCooldownSeconds(0.7f)
            .startingLives(3)
            .allowDoubleJump(false)
            .bonusLifeScore(100)
            .scoreInterval(0.05f)
            .scoreStep(1)
            .nightStartScore(700)
            .nightCyclePeriod(700)
            .nightDuration(200)
            .gravity(1800f)
            .jumpVelocity(780f)
            .fastFallVelocity(-1800f)
            .fastFallGravityMultiplier(3.0f)
            .enablePause(true)
            .enableShield(true)
            .shieldScoreEvery(150)
            .shieldMaxCharges(2)
            .enableStomp(true)
            .stompBounceVelocity(620f)
            .stompMinHeightRatio(0.6f)
            .stompScoreBonus(10)
            .enableGhostMode(true)
            .ghostModeDurationSeconds(2.5f)
            .ghostModeCooldownSeconds(8f)
            .build();
    }

    public static final class Builder {
        private float baseSpeed = 260f;
        private float speedIncreasePerScore = 0.08f;
        private float spawnIntervalStart = 1.6f;
        private float spawnIntervalMin = 0.9f;
        private float spawnIntervalScoreFactor = 1500f;
        private float spawnSkipChance = 0.2f;
        private float birdSpawnChance = 0.25f;
        private float birdSpeedBonus = 50f;
        private float obstacleScaleMin = 1.0f;
        private float obstacleScaleMax = 1.0f;
        private float hitCooldownSeconds = 0.7f;
        private int startingLives = 3;
        private boolean allowDoubleJump = false;
        private int bonusLifeScore = 100;
        private float scoreInterval = 0.05f;
        private int scoreStep = 1;
        private int nightStartScore = 700;
        private int nightCyclePeriod = 700;
        private int nightDuration = 200;
        private float gravity = 1800f;
        private float jumpVelocity = 780f;
        private float fastFallVelocity = -1800f;
        private float fastFallGravityMultiplier = 3.0f;
        private boolean enablePause = true;
        private boolean enableShield = false;
        private int shieldScoreEvery = 150;
        private int shieldMaxCharges = 2;
        private boolean enableStomp = false;
        private float stompBounceVelocity = 620f;
        private float stompMinHeightRatio = 0.6f;
        private int stompScoreBonus = 10;
        private boolean enableGhostMode = false;
        private float ghostModeDurationSeconds = 2.5f;
        private float ghostModeCooldownSeconds = 8f;

        public Builder baseSpeed(float baseSpeed) {
            this.baseSpeed = baseSpeed;
            return this;
        }

        public Builder speedIncreasePerScore(float speedIncreasePerScore) {
            this.speedIncreasePerScore = speedIncreasePerScore;
            return this;
        }

        public Builder spawnIntervalStart(float spawnIntervalStart) {
            this.spawnIntervalStart = spawnIntervalStart;
            return this;
        }

        public Builder spawnIntervalMin(float spawnIntervalMin) {
            this.spawnIntervalMin = spawnIntervalMin;
            return this;
        }

        public Builder spawnIntervalScoreFactor(float spawnIntervalScoreFactor) {
            this.spawnIntervalScoreFactor = spawnIntervalScoreFactor;
            return this;
        }

        public Builder spawnSkipChance(float spawnSkipChance) {
            this.spawnSkipChance = spawnSkipChance;
            return this;
        }

        public Builder birdSpawnChance(float birdSpawnChance) {
            this.birdSpawnChance = birdSpawnChance;
            return this;
        }

        public Builder birdSpeedBonus(float birdSpeedBonus) {
            this.birdSpeedBonus = birdSpeedBonus;
            return this;
        }

        public Builder obstacleScaleMin(float obstacleScaleMin) {
            this.obstacleScaleMin = obstacleScaleMin;
            return this;
        }

        public Builder obstacleScaleMax(float obstacleScaleMax) {
            this.obstacleScaleMax = obstacleScaleMax;
            return this;
        }

        public Builder hitCooldownSeconds(float hitCooldownSeconds) {
            this.hitCooldownSeconds = hitCooldownSeconds;
            return this;
        }

        public Builder startingLives(int startingLives) {
            this.startingLives = startingLives;
            return this;
        }

        public Builder allowDoubleJump(boolean allowDoubleJump) {
            this.allowDoubleJump = allowDoubleJump;
            return this;
        }

        public Builder bonusLifeScore(int bonusLifeScore) {
            this.bonusLifeScore = bonusLifeScore;
            return this;
        }

        public Builder scoreInterval(float scoreInterval) {
            this.scoreInterval = scoreInterval;
            return this;
        }

        public Builder scoreStep(int scoreStep) {
            this.scoreStep = scoreStep;
            return this;
        }

        public Builder nightStartScore(int nightStartScore) {
            this.nightStartScore = nightStartScore;
            return this;
        }

        public Builder nightCyclePeriod(int nightCyclePeriod) {
            this.nightCyclePeriod = nightCyclePeriod;
            return this;
        }

        public Builder nightDuration(int nightDuration) {
            this.nightDuration = nightDuration;
            return this;
        }

        public Builder gravity(float gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder jumpVelocity(float jumpVelocity) {
            this.jumpVelocity = jumpVelocity;
            return this;
        }

        public Builder fastFallVelocity(float fastFallVelocity) {
            this.fastFallVelocity = fastFallVelocity;
            return this;
        }

        public Builder fastFallGravityMultiplier(float fastFallGravityMultiplier) {
            this.fastFallGravityMultiplier = fastFallGravityMultiplier;
            return this;
        }

        public Builder enablePause(boolean enablePause) {
            this.enablePause = enablePause;
            return this;
        }

        public Builder enableShield(boolean enableShield) {
            this.enableShield = enableShield;
            return this;
        }

        public Builder shieldScoreEvery(int shieldScoreEvery) {
            this.shieldScoreEvery = shieldScoreEvery;
            return this;
        }

        public Builder shieldMaxCharges(int shieldMaxCharges) {
            this.shieldMaxCharges = shieldMaxCharges;
            return this;
        }

        public Builder enableStomp(boolean enableStomp) {
            this.enableStomp = enableStomp;
            return this;
        }

        public Builder stompBounceVelocity(float stompBounceVelocity) {
            this.stompBounceVelocity = stompBounceVelocity;
            return this;
        }

        public Builder stompMinHeightRatio(float stompMinHeightRatio) {
            this.stompMinHeightRatio = stompMinHeightRatio;
            return this;
        }

        public Builder stompScoreBonus(int stompScoreBonus) {
            this.stompScoreBonus = stompScoreBonus;
            return this;
        }

        public Builder enableGhostMode(boolean enableGhostMode) {
            this.enableGhostMode = enableGhostMode;
            return this;
        }

        public Builder ghostModeDurationSeconds(float ghostModeDurationSeconds) {
            this.ghostModeDurationSeconds = ghostModeDurationSeconds;
            return this;
        }

        public Builder ghostModeCooldownSeconds(float ghostModeCooldownSeconds) {
            this.ghostModeCooldownSeconds = ghostModeCooldownSeconds;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(this);
        }
    }
}
