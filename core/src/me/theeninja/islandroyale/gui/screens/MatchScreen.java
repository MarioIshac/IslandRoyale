package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandPerson;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.files.FileHandle;

public class MatchScreen implements Screen {
    private final Batch batch;
    private final Game game;

    private final MatchMap matchMap;

    private final Map<IslandTileType, Texture> islandTextures = new HashMap<>();

    public MatchScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.matchMap = new MatchMap(1000, 1000);

        for (IslandTileType islandTileType : IslandTileType.values()) {
            String texturePath = islandTileType.getTexturePath();

            if (texturePath == null)
                continue;

            FileHandle textureFileHandler = Gdx.files.internal(texturePath);
            Texture texture = new Texture(textureFileHandler);

            getIslandTextures().put(islandTileType, texture);
        }

        int centerFocusX = 60;
        int centerFocusY = 60;

        getMatchMap().getBottomLeft().set(centerFocusX, centerFocusY);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);
        getBatch().begin();

        Rectangle focusedMapRectangle = new Rectangle(
                getMatchMap().getBottomLeft().x,
                getMatchMap().getBottomLeft().y,
                Gdx.graphics.getWidth() / 16,
                Gdx.graphics.getHeight() / 16
        );

        System.out.println("Focused Map Rectangle: " + focusedMapRectangle);

        getMatchMap().getIslands().forEach((island, point) -> {
            System.out.println(island);

            Rectangle islandRectangle = new Rectangle(
                point.x,
                point.y,
                island.getMaxWidth(),
                island.getMaxHeight()
            );

            System.out.println("Island rectangle: " + islandRectangle);

            // Signifies that the island is indeed present in the focused part of the map
            if (focusedMapRectangle.overlaps(islandRectangle)) {

                for (int islandTileX = 0; islandTileX < islandRectangle.getWidth(); islandTileX++) {
                    for (int islandTileY = 0; islandTileY < islandRectangle.getHeight(); islandTileY++) {

                        // These refer to the absolute positions that each tile of the island has
                        // within the WHOLE map (not just focused)
                        int absoluteTileX = islandTileX + point.x;
                        int absoluteTileY = islandTileY + point.y;

                        // These refer to the position that each tile of the island has
                        // relative to the bottom left of the focused part of the map.
                        int relativeXToFocusPoint = absoluteTileX - getMatchMap().getBottomLeft().x;
                        int relativeYToFocusPoint = absoluteTileY - getMatchMap().getBottomLeft().y;

                        // Though we have verified that some part of the island is within the
                        // focused part of the map, it is is still possible that SOME of the island's tiles
                        // are NOT in focus.
                        boolean isXOutOfFocus = relativeXToFocusPoint < 0;
                        boolean isYOutOfFocus = relativeYToFocusPoint < 0;

                        // No need to render these said tiles of the island
                        if (isXOutOfFocus || isYOutOfFocus)
                            continue;

                        IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                        // Do not go into statement if island tile type is water i.e null
                        if (islandTileType != null) {
                            Texture islandTexture = getIslandTextures().get(islandTileType);

                            int absoluteCoordX = absoluteTileX * 16;
                            int absoluteCoordY = absoluteTileY * 16;

                            batch.draw(islandTexture, absoluteCoordX, absoluteCoordY);
                        }
                    }
                }
            }
        });

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
        getBatch().dispose();
    }

    public Game getGame() {
        return game;
    }

    public Batch getBatch() {
        return batch;
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }

    public Map<IslandTileType, Texture> getIslandTextures() {
        return islandTextures;
    }
}
