package com.exemple.joc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.exemple.joc.Main;

public class GameScreen implements Screen {

    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 480f;

    private static final float GROUND_Y = 90f;
    private static final float PLAYER_X = 90f;

    private static final float PLAYER_WIDTH = 64f;
    private static final float PLAYER_HEIGHT = 72f;
    private static final float DUCK_HEIGHT = 48f;

    private static final float GRAVITY = 1800f;
    private static final float JUMP_VELOCITY = 780f;

    private final Main game;
    private final SpriteBatch batch;

    private Texture playerTexture;
    private Texture cactusTexture;
    private Texture pteranodonTexture;
    private Texture backgroundDay;
    private Texture backgroundNight;

    private Sound jumpSound;
    private Sound hitSound;
    private Sound scoreSound;

    private BitmapFont font;

    private final Rectangle playerRect = new Rectangle();
    private final Array<Obstacle> obstacles = new Array<>();

    private float playerY = GROUND_Y;
    private float playerVelocityY = 0f;
    private boolean ducking = false;
    private boolean gameOver = false;

    private int score = 0;
    private int lives = 3;
    private int lastBonusLifeScore = -1;

    private float scoreTimer = 0f;
    private float obstacleTimer = 0f;
    private float spawnInterval = 1.3f;
    private float hitCooldown = 0f;

    private final float baseSpeed = 260f;
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
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

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));
        scoreSound = Gdx.audio.newSound(Gdx.files.internal("sounds/score.mp3"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
                game.setScreen(new MenuScreen(game));
            }
            return;
        }

        // CONTROLES
        boolean jumpPressed = Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.UP);

        // SPACE solo en escritorio (evita teclado en Android)
        if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Android
            && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jumpPressed = true;
        }

        if (jumpPressed && isOnGround()) {
            playerVelocityY = JUMP_VELOCITY;
            jumpSound.play(0.7f);
        }


        ducking = Gdx.input.isKeyPressed(Input.Keys.DOWN) && isOnGround();

        // FÍSICA DEL SALTO
        playerVelocityY -= GRAVITY * delta;
        playerY += playerVelocityY * delta;

        if (playerY < GROUND_Y) {
            playerY = GROUND_Y;
            playerVelocityY = 0f;
        }

        // PUNTUACIÓN
        scoreTimer += delta;
        if (scoreTimer >= 0.03f) {
            score++;
            scoreTimer = 0f;

            if (score > 0 && score % 100 == 0 && score != lastBonusLifeScore) {
                lives++;
                lastBonusLifeScore = score;
                scoreSound.play(0.7f);
            }
        }

        // VELOCIDAD Y DIFICULTAD
        float speed = baseSpeed + (score * 0.08f);
        spawnInterval = MathUtils.clamp(1.3f - (score / 1200f), 0.6f, 1.3f);

        // CREAR OBSTÁCULOS
        obstacleTimer += delta;
        if (obstacleTimer >= spawnInterval) {
            spawnObstacle(speed);
            obstacleTimer = 0f;
        }

        // MOVER OBSTÁCULOS
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            obstacle.x -= obstacle.speed * delta;
            obstacle.rect.setPosition(obstacle.x, obstacle.y);

            if (obstacle.x + obstacle.width < -100f) {
                obstacles.removeIndex(i);
            }
        }

        // COLISIONES
        if (hitCooldown > 0f) {
            hitCooldown -= delta;
        }

        updatePlayerRect();

        if (hitCooldown <= 0f) {
            for (int i = obstacles.size - 1; i >= 0; i--) {
                Obstacle obstacle = obstacles.get(i);
                if (playerRect.overlaps(obstacle.rect)) {
                    lives--;
                    hitSound.play(0.8f);
                    obstacles.removeIndex(i);
                    hitCooldown = 0.7f;

                    if (lives <= 0) {
                        gameOver = true;
                    }
                    break;
                }
            }
        }
    }

    private void spawnObstacle(float speed) {
        boolean bird = MathUtils.randomBoolean();

        if (bird) {
            float width = 70f;
            float height = 50f;
            float y = MathUtils.randomBoolean() ? GROUND_Y + 115f : GROUND_Y + 150f;

            Obstacle obstacle = new Obstacle(
                pteranodonTexture,
                WORLD_WIDTH + 40f,
                y,
                width,
                height,
                speed + 50f
            );
            obstacles.add(obstacle);
        } else {
            float width = 55f;
            float height = 75f;

            Obstacle obstacle = new Obstacle(
                cactusTexture,
                WORLD_WIDTH + 40f,
                GROUND_Y,
                width,
                height,
                speed
            );
            obstacles.add(obstacle);
        }
    }

    private void updatePlayerRect() {
        float drawHeight = ducking ? DUCK_HEIGHT : PLAYER_HEIGHT;
        playerRect.set(PLAYER_X, playerY, PLAYER_WIDTH, drawHeight);
    }

    private boolean isOnGround() {
        return playerY <= GROUND_Y + 0.01f;
    }

    private boolean isNight() {
        return score >= 700 && (score % 700) < 200;
    }

    private void draw() {
        boolean night = isNight();

        ScreenUtils.clear(night ? 0.05f : 0.60f, night ? 0.05f : 0.85f, night ? 0.10f : 0.95f, 1f);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // FONDO
        if (night) {
            batch.draw(backgroundNight, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        } else {
            batch.draw(backgroundDay, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }

        // JUGADOR
        float playerDrawHeight = ducking ? DUCK_HEIGHT : PLAYER_HEIGHT;
        batch.draw(playerTexture, PLAYER_X, playerY, PLAYER_WIDTH, playerDrawHeight);

        // OBSTÁCULOS
        for (Obstacle obstacle : obstacles) {
            batch.draw(obstacle.texture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // TEXTO HUD
        font.setColor(night ? Color.WHITE : Color.BLACK);
        font.draw(batch, "Punts: " + score, 20, WORLD_HEIGHT - 20);
        font.draw(batch, "Vides: " + lives, 20, WORLD_HEIGHT - 55);

        if (night) {
            font.draw(batch, "NIT", WORLD_WIDTH - 90, WORLD_HEIGHT - 20);
        } else {
            font.draw(batch, "DIA", WORLD_WIDTH - 90, WORLD_HEIGHT - 20);
        }

        if (gameOver) {
            font.draw(batch, "GAME OVER", WORLD_WIDTH / 2f - 90, WORLD_HEIGHT / 2f + 20);
            font.draw(batch, "Prem ENTER per tornar al menu", WORLD_WIDTH / 2f - 180, WORLD_HEIGHT / 2f - 20);
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
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();

        playerTexture.dispose();
        cactusTexture.dispose();
        pteranodonTexture.dispose();
        backgroundDay.dispose();
        backgroundNight.dispose();

        jumpSound.dispose();
        hitSound.dispose();
        scoreSound.dispose();

        font.dispose();
    }

    private static class Obstacle {
        Texture texture;
        float x;
        float y;
        float width;
        float height;
        float speed;
        Rectangle rect;

        Obstacle(Texture texture, float x, float y, float width, float height, float speed) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.rect = new Rectangle(x, y, width, height);
        }
    }
}
