package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import me.theeninja.islandroyale.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.badlogic.gdx.files.FileHandle;
import me.theeninja.islandroyale.treasure.DataTreasure;
import me.theeninja.islandroyale.treasure.ResourceTreasure;
import me.theeninja.islandroyale.treasure.Treasure;

public class MatchScreen implements Screen {
    private final Batch batch;
    private final Game game;

    private final MatchMap matchMap;
    private final Player player;

    private final Map<IslandTileType, Texture> islandTextures = new HashMap<>();
    private final Map<Resource, Texture> treasureTextures = new HashMap<>();
    private final Map<Resource, Texture> resourceTextures = new HashMap<>();

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

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int numberOfIslands = getMatchMap().getIslands().size();
        int randomIslandNumber = MathUtils.random(numberOfIslands - 1);

        List<Island> islands = new ArrayList<>(getMatchMap().getIslands().keySet());

        Island chosenIsland = islands.get(randomIslandNumber);

        player = new Player(chosenIsland);


        System.out.println("Chosen Island Position " + getMatchMap().getIslands().get(chosenIsland));
    }

    @Override
    public void show() {

    }

    static int i = 0;

    @Override
    public void render(float delta) {
        if (i == 10)  {
            getMatchMap().getBottomLeft().sub(1.0f, 1.0f, 0);
            i = 0;
        }

        i++;

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
            Rectangle islandRectangle = new Rectangle(
                point.x,
                point.y,
                island.getMaxWidth(),
                island.getMaxHeight()
            );

            // Signifies that the island is indeed present in the focused part of the map
            if (focusedMapRectangle.overlaps(islandRectangle)) {
                System.out.println("Island rectangle: " + islandRectangle);

                for (int islandTileX = 0; islandTileX < islandRectangle.getWidth(); islandTileX++) {
                    for (int islandTileY = 0; islandTileY < islandRectangle.getHeight(); islandTileY++) {

                        // These refer to the absolute positions that each tile of the island has
                        // within the WHOLE map (not just focused)
                        int absoluteTileX = islandTileX + (int) point.x;
                        int absoluteTileY = islandTileY + (int) point.y;

                        // These refer to the position that each tile of the island has
                        // relative to the bottom left of the focused part of the map.
                        int relativeXToFocusPoint = absoluteTileX - (int) getMatchMap().getBottomLeft().x;
                        int relativeYToFocusPoint = absoluteTileY - (int) getMatchMap().getBottomLeft().y;

                        // Though we have verified that some part of the island is within the
                        // focused part of the map, it is is still possible that SOME of the island's tiles
                        // are NOT in focus.
                        boolean isXOutOfFocus = relativeXToFocusPoint < 0;
                        boolean isYOutOfFocus = relativeYToFocusPoint < 0;

                        // No need to render these said tiles of the island
                        if (isXOutOfFocus || isYOutOfFocus)
                            continue;

                        if (islandTileX == 0 && islandTileY == 0) {
                            System.out.println("Relative X " + relativeXToFocusPoint);
                            System.out.println("Relative Y " + relativeYToFocusPoint);
                        }

                        IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                        // Do not go into statement if island tile type is water i.e null
                        if (islandTileType != null) {
                            Texture islandTexture = getIslandTextures().get(islandTileType);

                            int relativeCoordXToFocusPoint = relativeXToFocusPoint * 16;
                            int relativeCoordYToFocusPoint = relativeYToFocusPoint * 16;

                            getBatch().draw(islandTexture, relativeCoordXToFocusPoint, relativeCoordYToFocusPoint);
                        }
                    }
                }
            }
        });

        if (Gdx.input.justTouched()) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.line(0, 0, 20, 20);
            shapeRenderer.end();
        }

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

    public Player getPlayer() {
        return player;
    }
}
