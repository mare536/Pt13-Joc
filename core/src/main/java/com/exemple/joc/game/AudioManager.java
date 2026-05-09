package com.exemple.joc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private final Sound jumpSound;
    private final Sound hitSound;
    private final Sound scoreSound;

    public AudioManager() {
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));
        scoreSound = Gdx.audio.newSound(Gdx.files.internal("sounds/score.mp3"));
    }

    public void playJump() {
        jumpSound.play(0.7f);
    }

    public void playHit() {
        hitSound.play(0.8f);
    }

    public void playScore() {
        scoreSound.play(0.7f);
    }

    public void dispose() {
        jumpSound.dispose();
        hitSound.dispose();
        scoreSound.dispose();
    }
}
