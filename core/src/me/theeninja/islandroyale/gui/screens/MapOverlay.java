package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;

import java.util.HashMap;
import java.util.Map;

public class MapOverlay {
    private final MatchMap matchMap;
    private final MatchScreen matchScreen;
    private static final float DOWN_SCALE = 0.3f;
    private final Camera camera;
    private final Viewport viewport;
    private final Batch batch = new SpriteBatch();

    public MapOverlay(MatchMap matchMap, MatchScreen matchScreen) {
        this.matchMap = matchMap;
        this.matchScreen = matchScreen;
        this.camera = new OrthographicCamera();
        this.viewport = new FillViewport(getMatchMap().getTileWidth(), getMatchMap().getTileHeight(), getCamera());
        getCamera().position.set(getMatchMap().getTileWidth() / 2f, getMatchMap().getTileHeight() / 2f, 0);

        fillBareMap();
    }

    private final Map<Vector2, IslandTileType> bareMap = new HashMap<>();

    public void fillBareMap() {
        for (Island island : getMatchMap().getIslands()) {
            float x = island.getPositionOnMap().x;
            float y = island.getPositionOnMap().y;

            for (int relX = 0; relX < island.getMaxWidth(); relX++) {
                for (int relY = 0; relY < island.getMaxHeight(); relY++) {
                    float absoluteX = x + relX;
                    float absoluteY = y + relY;

                    float xDistanceFromCenter = absoluteX / getMatchMap().getTileWidth() - 0.5f;
                    float yDistanceFromCenter = absoluteY / getMatchMap().getTileHeight() - 0.5f;

                    // Perform downscales
                    xDistanceFromCenter *= DOWN_SCALE;
                    yDistanceFromCenter *= DOWN_SCALE;

                    System.out.println(absoluteX + " X distance from center " + xDistanceFromCenter);
                    System.out.println(absoluteY + " Y distance from center " + yDistanceFromCenter);

                    float finalX = getMatchMap().getTileWidth() / 2 + xDistanceFromCenter * getMatchMap().getTileWidth();
                    float finalY = getMatchMap().getTileHeight() / 2 + yDistanceFromCenter * getMatchMap().getTileHeight();

                    System.out.println(absoluteX + " final X: " + finalX);
                    System.out.println(absoluteY + " final Y: " + finalY);

                    Vector2 vector2 = new Vector2(finalX, finalY);
                    getBareMap().put(vector2, island.getRepr()[relX][relY]);

                }
            }
        }

    }

    public void draw() {
        long a = System.currentTimeMillis();

        getCamera().update();
        getBatch().setProjectionMatrix(getCamera().combined);

        for (Map.Entry<Vector2, IslandTileType> entry : getBareMap().entrySet()) {
            if (entry.getValue() == null)
                continue;

            getMatchScreen().getBatch().draw(
                    getMatchScreen().getIslandTextures().get(entry.getValue()),
                    entry.getKey().x,
                    entry.getKey().y,

                    // Default 1*1 tile width and height, accounting for down scale
                    DOWN_SCALE,
                    DOWN_SCALE
            );
        }

        System.out.println(System.currentTimeMillis() - a);
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }

    public MatchScreen getMatchScreen() {
        return matchScreen;
    }

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Map<Vector2, IslandTileType> getBareMap() {
        return bareMap;
    }

    public Batch getBatch() {
        return batch;
    }
}
