package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;

public class MapOverlay {
    private final MatchMap matchMap;
    private final MatchScreen matchScreen;
    private static final float DOWN_SCALE = 0.8f;
    private final Camera camera;

    public MapOverlay(MatchMap matchMap, MatchScreen matchScreen) {
        this.matchMap = matchMap;
        this.matchScreen = matchScreen;
        this.camera = new OrthographicCamera(getMatchMap().getTileWidth(), getMatchMap().getTileHeight());
        getCamera().position.set(getMatchMap().getTileWidth() / 2f, getMatchMap().getTileHeight() / 2f, 0);
    }

    public void draw() {
        getMatchScreen().getBatch().setProjectionMatrix(getCamera().combined);

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

                    System.out.println(x + " X distance from center " + xDistanceFromCenter);
                    System.out.println(y + " Y distance from center " + yDistanceFromCenter);

                    float finalX = getMatchMap().getTileWidth() / 2 + xDistanceFromCenter * getMatchMap().getTileWidth();
                    float finalY = getMatchMap().getTileHeight() / 2 + yDistanceFromCenter * getMatchMap().getTileHeight();

                    System.out.println(x + " final X: " + finalX);
                    System.out.println(y + " final Y: " + finalY);

                    getMatchScreen().getBatch().draw(
                            getMatchScreen().getIslandTextures().get(IslandTileType.DIRT),
                            finalX,
                            finalY,

                            // Default 1*1 tile width and height
                            1,
                            1
                    );
                }
            }
        }

        getMatchScreen().getBatch().setProjectionMatrix(getMatchScreen().getMapCamera().combined);
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
}
