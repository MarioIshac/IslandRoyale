package me.theeninja.islandroyale;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.gui.SpriteSheet;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

import static me.theeninja.islandroyale.IslandTileType.*;

public class IslandRoyaleGame extends Game {
    @Override
    public void create() {
        Screen matchScreen = new MatchScreen(this);

        setScreen(matchScreen);
    }

    @Override
    public void render() {
        super.render();
    }
}
