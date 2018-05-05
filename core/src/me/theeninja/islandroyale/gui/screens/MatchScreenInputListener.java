package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MatchScreenInputListener implements InputProcessor {

    private final MatchScreen matchScreen;

    private static final int SHOW_MAP_KEY = Input.Keys.M;
    private boolean mapShown;

    MatchScreenInputListener(MatchScreen matchScreen) {
        this.matchScreen = matchScreen;
    }

    @Override
    public boolean keyDown( int keyCode) {
        switch (keyCode) {
            case SHOW_MAP_KEY: {
                System.out.println("Showing map");
                setMapShown(true);
                return true;
            }

            default: return false;
        }
    }

    @Override
    public boolean keyUp(int keyCode) {
        switch (keyCode) {
            case SHOW_MAP_KEY: {
                System.out.println("Hiding map");
                setMapShown(false);
                return true;
            }

            default: return false;
        }
    }

    public MatchScreen getMatchScreen() {
        return matchScreen;
    }

    public boolean isMapShown() {
        return mapShown;
    }

    public void setMapShown(boolean mapShown) {
        this.mapShown = mapShown;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
