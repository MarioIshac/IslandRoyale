package me.theeninja.islandroyale;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import me.theeninja.islandroyale.entity.InventorySerializer;
import me.theeninja.islandroyale.entity.building.OffenseBuildingType;
import me.theeninja.islandroyale.gui.screens.HomeScreen;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public class IslandRoyaleGame extends Game {
    @Override
    public void create() {
        Screen matchScreen = new MatchScreen(this);
        Screen homeScreen = new HomeScreen(this);

        setScreen(homeScreen);
    }

    private static final int FULLSCREEN_KEY = Keys.F11;

    @Override
    public void render() {
        super.render();

        // Toggle Fullscreen
        if (Gdx.input.isKeyJustPressed(FULLSCREEN_KEY))
            toggleFullScreen();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public static final Json JSON = new Json();

    static {
        JSON.setOutputType(JsonWriter.OutputType.json);
        JSON.setSerializer(Inventory.class, new InventorySerializer());
    }

    public void toggleFullScreen() {
        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        if (Gdx.graphics.isFullscreen())
            Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
        else
            Gdx.graphics.setFullscreenMode(displayMode);
    }
}
