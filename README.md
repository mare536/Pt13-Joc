# Pt13-Joc del [Mario, Sonic, Batman, Joker o un altre personatge]

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `android`: Android mobile platform. Needs Android SDK.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

## Game mode presets

In `core/src/main/java/com/exemple/joc/screens/GameScreen.java`, inside the constructor,
change the line that contains `this.gameConfig =` (for example: `this.gameConfig = GameConfig.normal();`).

Available presets:
- `GameConfig.normal()` (balanced default)
- `GameConfig.easy()` (slower and more lives)
- `GameConfig.hard()` (faster and fewer lives)
- `GameConfig.doubleJump()` (double jump)
- `GameConfig.denseObstacles()` (more frequent obstacles and higher bird chance)
- `GameConfig.floaty()` (lower gravity + double jump)
- `GameConfig.endlessNight()` (night mode from the start)
- `GameConfig.rapidScore()` (score increases faster)
- `GameConfig.custom()` (for your own change)

To customize, edit the values inside the `custom()` method in
`core/src/main/java/com/exemple/joc/game/GameConfig.java`.

Advanced knobs you can tweak in `custom()`:
- `baseSpeed` / `speedIncreasePerScore`: starting speed and how fast it ramps up
- `spawnIntervalStart` / `spawnIntervalMin` / `spawnIntervalScoreFactor`: obstacle timing curve
- `spawnSkipChance`: chance to skip a spawn (0 = always spawn)
- `birdSpawnChance` / `birdSpeedBonus`: more birds and how fast they are
- `obstacleScaleMin` / `obstacleScaleMax`: random size range for obstacles
- `hitCooldownSeconds`: invulnerability time after a hit
- `startingLives` / `allowDoubleJump`: lives and jump mode
- `bonusLifeScore`: points needed for a bonus life (0 disables)
- `scoreInterval` / `scoreStep`: score speed and increments
- `nightStartScore` / `nightCyclePeriod` / `nightDuration`: control day/night cycle
- `gravity` / `jumpVelocity` / `fastFallVelocity` / `fastFallGravityMultiplier`: player physics
