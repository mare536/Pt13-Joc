package com.exemple.joc.game;

public class GameConfig {
    public final float baseSpeed;
    public final float spawnIntervalStart;
    public final float spawnIntervalMin;
    public final float spawnIntervalScoreFactor;
    public final int startingLives;
    public final boolean allowDoubleJump;

    private GameConfig(float baseSpeed, float spawnIntervalStart, float spawnIntervalMin,
                       float spawnIntervalScoreFactor, int startingLives, boolean allowDoubleJump) {
        this.baseSpeed = baseSpeed;
        this.spawnIntervalStart = spawnIntervalStart;
        this.spawnIntervalMin = spawnIntervalMin;
        this.spawnIntervalScoreFactor = spawnIntervalScoreFactor;
        this.startingLives = startingLives;
        this.allowDoubleJump = allowDoubleJump;
    }

    public static GameConfig normal() {
        return new GameConfig(260f, 1.6f, 0.9f, 1500f, 3, false);
    }

    public static GameConfig easy() {
        return new GameConfig(220f, 1.9f, 1.2f, 1800f, 5, false);
    }

    public static GameConfig hard() {
        return new GameConfig(310f, 1.3f, 0.7f, 1200f, 2, false);
    }

    public static GameConfig doubleJump() {
        return new GameConfig(260f, 1.6f, 0.9f, 1500f, 3, true);
    }

    public static GameConfig custom() {
        return new GameConfig(260f, 1.6f, 0.9f, 1500f, 3, false);
    }
}
