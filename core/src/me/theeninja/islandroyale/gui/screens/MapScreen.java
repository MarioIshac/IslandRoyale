package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import me.theeninja.islandroyale.MatchMap;

public class MapScreen implements Screen {
    private final Game game;
    private final MatchMap matchMap;

    public MapScreen(Game game, MatchMap matchMap) {
        this.game = game;
        this.matchMap = matchMap;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Game getGame() {
        return game;
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }
}
