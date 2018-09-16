package me.theeninja.islandroyale;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public class OfflineMatchButtonListener extends InputListener {
    private final Game game;

    public OfflineMatchButtonListener(Game game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        MatchScreen matchScreen = new MatchScreen(getGame());

        getGame().setScreen(matchScreen);

        return true;
    }

    public Game getGame() {
        return game;
    }
}
