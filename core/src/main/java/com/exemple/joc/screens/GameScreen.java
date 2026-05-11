package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.exemple.joc.Main;
import com.exemple.joc.game.AudioManager;
import com.exemple.joc.game.GameConfig;
import com.exemple.joc.game.Obstacle;
import com.exemple.joc.game.Player;
import com.exemple.joc.game.ScoreManager;

public class GameScreen implements Screen {

    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;
    private static final float CACTUS_WIDTH = 60f;
    private static final float CACTUS_HEIGHT = 70f;
    private static final float CACTUS_Y_OFFSET = -10f;
    private static final float PTERA_LOW_Y = 110f;
    private static final float GROUND_Y = 90f;

    private final Main game;
    private final SpriteBatch batch;
    private final AudioManager audioManager;
    private final ScoreManager scoreManager;
    private final GameConfig gameConfig;
    private final Player player;
    private final Array<Obstacle> obstacles = new Array<>();

    private Texture playerTexture;
    private Texture cactusTexture;
    private Texture pteranodonTexture;
    private Texture backgroundDay;
    private Texture backgroundNight;
    private boolean gameOver = false;
    private float obstacleTimer = 0f;
    private float hitCooldown = 0f;
    private boolean lastJumpKeyPressed = false;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    public GameScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.audioManager = new AudioManager();
        this.gameConfig = GameConfig.normal();
        this.scoreManager = new ScoreManager(gameConfig);
        this.player = new Player(gameConfig);
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0f);
        camera.update();
        loadAssets();
    }

    private void loadAssets() {
        playerTexture = new Texture("images/player.png");
        cactusTexture = new Texture("images/cactus.png");
        pteranodonTexture = new Texture("images/pteranodon.png");

        backgroundDay = new Texture("images/background_day.png");
        backgroundNight = new Texture("images/background_night.png");
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        if (gameOver) {
            if (Gdx.input.justTouched()) {
                game.setScreen(new MenuScreen(game));
            }
            return;
        }

        boolean isAndroid = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android;
        boolean jumpPressed = readJumpPressed(isAndroid);
        boolean downPressed = readDownPressed(isAndroid);

        if (jumpPressed) {
            player.jump(audioManager);
        }
        player.update(downPressed, delta);
        scoreManager.update(delta, player, audioManager);
        updateDifficultyAndSpawning(delta);
        updateObstacles(delta);
        updateHitCooldown(delta);
        checkCollisions();
    }

    private boolean readJumpPressed(boolean isAndroid) {
        boolean currentJumpKeyPressed = Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.SPACE);
        boolean jumpPressed = currentJumpKeyPressed && !lastJumpKeyPressed;
        lastJumpKeyPressed = currentJumpKeyPressed;

        if (isAndroid) {
            boolean rightJustTouched = false;
            int screenMid = Gdx.graphics.getWidth() / 2;

            for (int p = 0; p < 5; p++) {
                if (Gdx.input.isTouched(p) && Gdx.input.getX(p) >= screenMid) {
                    rightJustTouched = true;
                    break;
                }
            }

            if (Gdx.input.justTouched() && Gdx.input.getX(0) >= screenMid) {
                rightJustTouched = true;
            }

            return jumpPressed || rightJustTouched;
        }

        return jumpPressed || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.justTouched();
    }

    private boolean readDownPressed(boolean isAndroid) {
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (!isAndroid) {
            return downPressed;
        }

        boolean leftTouched = false;
        int screenMid = Gdx.graphics.getWidth() / 2;

        for (int p = 0; p < 5; p++) {
            if (Gdx.input.isTouched(p) && Gdx.input.getX(p) < screenMid) {
                leftTouched = true;
                break;
            }
        }

        return downPressed || leftTouched;
    }

    private void updateDifficultyAndSpawning(float delta) {
        float speed = gameConfig.baseSpeed + (scoreManager.getScore() * gameConfig.speedIncreasePerScore);
        float spawnInterval = MathUtils.clamp(
            gameConfig.spawnIntervalStart - (scoreManager.getScore() / gameConfig.spawnIntervalScoreFactor),
            gameConfig.spawnIntervalMin,
            gameConfig.spawnIntervalStart
        );

        obstacleTimer += delta;
        if (obstacleTimer >= spawnInterval) {
            spawnObstacle(speed);
            obstacleTimer = 0f;
        }
    }

    private void updateObstacles(float delta) {
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            obstacle.update(delta);

            if (obstacle.isOffScreen(-100f)) {
                obstacles.removeIndex(i);
            }
        }
    }

    private void updateHitCooldown(float delta) {
        if (hitCooldown > 0f) {
            hitCooldown -= delta;
        }
    }

    private void checkCollisions() {
        if (hitCooldown > 0f) {
            return;
        }

        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            if (!obstacle.overlaps(player.getBounds())) {
                continue;
            }

            player.loseLife();
            audioManager.playHit();
            obstacles.removeIndex(i);
            hitCooldown = gameConfig.hitCooldownSeconds;

            if (!player.hasLivesLeft()) {
                gameOver = true;
            }
            break;
        }
    }

    private void spawnObstacle(float speed) {
        if (MathUtils.random() < gameConfig.spawnSkipChance) {
            return;
        }
        boolean bird = MathUtils.random() < gameConfig.birdSpawnChance;
        float scale = MathUtils.random(gameConfig.obstacleScaleMin, gameConfig.obstacleScaleMax);

        if (bird) {
            float width = 70f * scale;
            float height = 50f * scale;
            float y = MathUtils.randomBoolean()
                    ? PTERA_LOW_Y
                    : (MathUtils.randomBoolean() ? GROUND_Y + 115f : GROUND_Y + 150f);

            Obstacle obstacle = new Obstacle(
                pteranodonTexture,
                WORLD_WIDTH + 40f,
                y,
                width,
                height,
                speed + gameConfig.birdSpeedBonus
            );
            obstacles.add(obstacle);
        } else {
            Obstacle obstacle = new Obstacle(
                cactusTexture,
                WORLD_WIDTH + 40f,
                GROUND_Y + CACTUS_Y_OFFSET,
                CACTUS_WIDTH * scale,
                CACTUS_HEIGHT * scale,
                speed
            );
            obstacles.add(obstacle);
        }
    }

    private void draw() {
        boolean night = scoreManager.isNight();

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        if (night) {
            batch.draw(backgroundNight, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        } else {
            batch.draw(backgroundDay, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }

        player.draw(batch, playerTexture);

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch);
        }

        scoreManager.drawHud(batch, player.getLives(), WORLD_WIDTH, WORLD_HEIGHT);

        if (gameOver) {
            scoreManager.drawGameOver(batch, WORLD_WIDTH, WORLD_HEIGHT);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        Gdx.input.setCatchKey(Input.Keys.UP, true);
        Gdx.input.setCatchKey(Input.Keys.DOWN, true);
        Gdx.input.setCatchKey(Input.Keys.SPACE, true);
        Gdx.input.setCatchKey(Input.Keys.ENTER, true);
        lastJumpKeyPressed = false;
        Gdx.input.setInputProcessor(null);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setCatchKey(Input.Keys.UP, false);
        Gdx.input.setCatchKey(Input.Keys.DOWN, false);
        Gdx.input.setCatchKey(Input.Keys.SPACE, false);
        Gdx.input.setCatchKey(Input.Keys.ENTER, false);
        lastJumpKeyPressed = false;
    }

    @Override
    public void dispose() {
        batch.dispose();

        playerTexture.dispose();
        cactusTexture.dispose();
        pteranodonTexture.dispose();
        backgroundDay.dispose();
        backgroundNight.dispose();
        audioManager.dispose();
        scoreManager.dispose();
    }
}
