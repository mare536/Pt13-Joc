package com.exemple.joc;

        import com.badlogic.gdx.Game;
        import com.exemple.joc.screens.SplashScreen;

        public class Main extends Game {
            @Override
            public void create() {
                setScreen(new SplashScreen(this));
            }
        }
