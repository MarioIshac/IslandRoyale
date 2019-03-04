package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.theeninja.islandroyale.OfflineMatchButtonListener;
import me.theeninja.islandroyale.entity.Skins;

public class HomeScreen implements Screen {
    private final Game game;
    private final Texture backgroundImage;
    private final Batch batch;
    private final Stage stage;

    private final static String BACKGROUND_IMAGE_LOCATION = "OceanBackground.jpg";

    private static final String NEW_OFFLINE_MATCH = "Offline Match";
    private static final String NEW_ONLINE_MATCH = "Online Match";
    private static final String SETTINGS = "Settings";

    private static final float WIDTH_SEPARATOR = 10f;
    private static final float HEIGHT_SEPARATOR = 10f;

    public HomeScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), getBatch());

        final FileHandle backgroundImageFileHandle = Gdx.files.internal(BACKGROUND_IMAGE_LOCATION);
        this.backgroundImage = new Texture(backgroundImageFileHandle);

        final Button newOfflineMatchButton = new TextButton(NEW_OFFLINE_MATCH, Skins.getInstance().getFlatEarthSkin());
        final Button newOnlineMatchButton = new TextButton(NEW_ONLINE_MATCH, Skins.getInstance().getFlatEarthSkin());
        final Button settingsButton = new TextButton(SETTINGS, Skins.getInstance().getFlatEarthSkin());

        getStage().addActor(newOfflineMatchButton);
        getStage().addActor(newOnlineMatchButton);
        getStage().addActor(settingsButton);

        final float halfWidth = Gdx.graphics.getWidth() / 2f;
        final float halfHeight = Gdx.graphics.getHeight() / 2f;

        newOfflineMatchButton.setPosition(
            halfWidth - WIDTH_SEPARATOR / 2 - newOfflineMatchButton.getWidth(),
            halfHeight + HEIGHT_SEPARATOR / 2 - newOfflineMatchButton.getHeight() / 2
        );

        newOnlineMatchButton.setPosition(
            halfWidth + WIDTH_SEPARATOR / 2,
            halfHeight + HEIGHT_SEPARATOR / 2 - newOnlineMatchButton.getHeight() / 2
        );

        settingsButton.setPosition(
            halfWidth - settingsButton.getWidth() / 2,
            halfHeight - HEIGHT_SEPARATOR / 2 - settingsButton.getHeight() * 3 / 2
        );

        final OfflineMatchButtonListener offlineMatchListener = new OfflineMatchButtonListener(getGame());
        newOfflineMatchButton.addListener(offlineMatchListener);

        Gdx.input.setInputProcessor(getStage());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getBatch().begin();
        getBatch().draw(getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        getBatch().end();

        getStage().act();
        getStage().draw();
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

    public Texture getBackgroundImage() {
        return backgroundImage;
    }

    public Game getGame() {
        return game;
    }

    public Batch getBatch() {
        return batch;
    }

    public Stage getStage() {
        return stage;
    }
}
