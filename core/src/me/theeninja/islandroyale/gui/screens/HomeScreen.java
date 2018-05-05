package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {

    private final Game game;
    private final Texture backgroundImage;
    private final Batch batch;
    private final Stage stage;

    private final static String BACKGROUND_IMAGE_LOCATION = "OceanBackground.jpg";

    public HomeScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), getBatch());

        FileHandle backgroundImageFileHandle = Gdx.files.internal(BACKGROUND_IMAGE_LOCATION);
        this.backgroundImage = new Texture(backgroundImageFileHandle);
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
}
