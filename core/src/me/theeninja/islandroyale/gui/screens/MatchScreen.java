package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.entity.*;

import java.util.*;

public class MatchScreen implements Screen {

    public final static Skin FLAT_EARTH_SKIN;

    static {
        FileHandle flatEarthSkinFileHandler = new FileHandle("flat-earth/skin/flat-earth-ui.json");
        FLAT_EARTH_SKIN = new Skin(flatEarthSkinFileHandler);
    }

    private final Game game;

    private final MatchMap matchMap;
    private final Player player;

    private final Stage stage;

    private final Map<IslandTileType, Texture> islandTextures = new HashMap<>();
    private final Map<Resource, Texture> treasureTextures = new HashMap<>();
    private final Map<Resource, Texture> resourceTextures = new HashMap<>();
    private final Map<Island, GridPoint2> visibleIslands = new HashMap<>();

    private final Texture checkmark;
    private final Texture x;

    private final Batch batch;

    private boolean isTouchDown;

    private final VerticalGroup buildMenu = new VerticalGroup();
    private final VerticalGroup resourceMenu = new VerticalGroup();

    Array<TransportEntityType> transportEntityTypes;

    public MatchScreen(Game game) {
        this.transportEntityTypes = EntityType.loadEntityTypes(TransportEntityType.class);

        this.batch = new SpriteBatch();

        this.game = game;

        ResourceBuildingType resourceBuildingType = new ResourceBuildingType();

        this.matchMap = new MatchMap(1000, 1000);
        this.stage = new Stage(new ScreenViewport(), getBatch());

        Gdx.input.setInputProcessor(getStage());

        FileHandle checkmarkFileHandle = Gdx.files.internal("CheckMark.png");
        FileHandle xFileHandle = Gdx.files.internal("X.png");

        this.checkmark = new Texture(checkmarkFileHandle);
        this.x = new Texture(xFileHandle);

        for (IslandTileType islandTileType : IslandTileType.values()) {
            String texturePath = islandTileType.getTexturePath();

            if (texturePath == null)
                continue;

            FileHandle textureFileHandler = Gdx.files.internal(texturePath);
            Texture texture = new Texture(textureFileHandler);

            getIslandTextures().put(islandTileType, texture);
        }


        int numberOfIslands = getMatchMap().getIslands().size();
        int randomIslandNumber = MathUtils.random(numberOfIslands - 1);

        List<Island> islands = new ArrayList<>(getMatchMap().getIslands().keySet());

        Island chosenIsland = islands.get(randomIslandNumber);

        this.player = new Player(chosenIsland);

        GridPoint2 bottomLeftFocusPos = this.getMatchMap().getIslands().get(chosenIsland).cpy();
        bottomLeftFocusPos.sub(20, 20);

        getMatchMap().getFocusOrigin().set(bottomLeftFocusPos);

        getResourceMenu().space(20f);
        getBuildMenu().space(20f);

        Array<BuildingEntityType<?>> buildingTypes = BuildingEntityType.loadBuildingEntityTypes();

        for (BuildingEntityType<?> buildingEntityType : buildingTypes) {
            BuildButton<?> buildButton = new BuildButton<>(buildingEntityType);
            getBuildMenu().addActor(buildButton);
        }

        for (BuildingEntityType<?> buildingType : buildingTypes) {

        }

        for (Resource resource : Resource.values()) {
            FileHandle fileHandle = Gdx.files.internal("resource/" + resource.name() + ".png");
            Texture texture = new Texture(fileHandle);
            getResourceTextures().put(resource, texture);

            ResourceLabel resourceLabel = new ResourceLabel(resource, getResourceTextures().get(resource), getPlayer());
            getResourceMenu().addActor(resourceLabel);

        }

        getBuildMenu().setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
        getResourceMenu().setPosition(100, Gdx.graphics.getHeight() - 20);

        getStage().addActor(getBuildMenu());
        getStage().addActor(getResourceMenu());

        getPlayer().getInventory().put(Resource.WOOD, 1000);
    }

    private static boolean isBuildButtonInDragState(BuildButton buildButton) {
        return buildButton.getBuildPosition() != null;
    }

    @Override
    public void show() {

    }

    private void drawIslands(Rectangle focusedMapRectangle) {
        getMatchMap().getIslands().forEach((island, point) -> {
            Rectangle islandRectangle = new Rectangle(
                    point.x,
                    point.y,
                    island.getMaxWidth(),
                    island.getMaxHeight()
            );

            // Signifies that the island is indeed present in the focused part of the map
            if (focusedMapRectangle.overlaps(islandRectangle))
                drawIsland(island);
        });
    }
    private void drawIsland(Island island) {
        // Refers to position of island relative to bottom left of focus rectangle
        GridPoint2 islandCoordsRelativeToFocus = getMatchMap().getIslands().get(island)
                // Must be copied since we do not want to modify position, it should be immutable
                .cpy();

        // Changes coord position from absolute to relative to focus
        islandCoordsRelativeToFocus.sub(getMatchMap().getFocusOrigin());

        getVisibleIslands().put(island, islandCoordsRelativeToFocus);

        for (int islandTileX = 0; islandTileX < island.getMaxWidth(); islandTileX++) {
            for (int islandTileY = 0; islandTileY < island.getMaxHeight(); islandTileY++) {
                // These refer to the position that each tile of the island has
                // relative to the bottom left of the focused part of the map.
                int relativeXToFocusPoint = islandCoordsRelativeToFocus.x + islandTileX;
                int relativeYToFocusPoint = islandCoordsRelativeToFocus.y + islandTileY;

                // Though we have verified that some part of the island is within the
                // focused part of the map, it is is still possible that SOME of the island's tiles
                // are NOT in focus.
                boolean isXOutOfFocus = relativeXToFocusPoint < 0;
                boolean isYOutOfFocus = relativeYToFocusPoint < 0;

                // No need to render these said tiles of the island
                if (isXOutOfFocus || isYOutOfFocus)
                    continue;

                IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                int relativeCoordXToFocusPoint = relativeXToFocusPoint * 16;
                int relativeCoordYToFocusPoint = relativeYToFocusPoint * 16;

                // Do not go into statement if island tile type is water i.e null
                if (islandTileType != null) {
                    Texture islandTexture = getIslandTextures().get(islandTileType);

                    getBatch().draw(islandTexture, relativeCoordXToFocusPoint, relativeCoordYToFocusPoint);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getBatch().begin();

        Rectangle focusedMapRectangle = new Rectangle(
                getMatchMap().getFocusOrigin().x,
                getMatchMap().getFocusOrigin().y,
                Gdx.graphics.getWidth() / 16,
                Gdx.graphics.getHeight() / 16
        );

        getVisibleIslands().clear();

        // This method has side effects of updating visible islands
        drawIslands(focusedMapRectangle);

        drawBuildings();

        getMatchMap().flushDeadEntities();

        for (TransportEntityType transportEntityType : transportEntityTypes) {
            Entity<MovingEntityType<TransportEntityType>> entity = new Entity<>(transportEntityType);
            getBatch().draw(entity.getType().getTexture(), 500, 500);
            getMatchMap().getEntities().clear();
            getMatchMap().getEntities().put(entity, new Vector2(500, 500));
        }

        for (int i = 0; i < getBuildMenu().getChildren().size; i++) {
            Actor actor = getBuildMenu().getChildren().get(i);

            if (!(actor instanceof BuildButton))
                continue;

            BuildButton<?> buildButton = (BuildButton<?>) actor;

            if (!isBuildButtonInDragState(buildButton))
                continue;

            handleBuildButton(buildButton, focusedMapRectangle);
        }

        for (Actor actor : getResourceMenu().getChildren()) {
            if (!(actor instanceof ResourceLabel))
                continue;

            ResourceLabel resourceLabel = (ResourceLabel) actor;

            resourceLabel.update(delta);
        }


        getBatch().end();

        updateBuildings(delta);

        getStage().draw();
    }

    private void drawBuildings() {
        getVisibleIslands().forEach((island, relativeToFocusPoint) -> {
            island.getBuildings().forEach((building, buildingOffset) -> {
                int newX = relativeToFocusPoint.x + buildingOffset.x;
                int newY = relativeToFocusPoint.y + buildingOffset.y;

                // Change from tiles to pixels
                newX *= 16;
                newY *= 16;

                getBatch().draw(building.getType().getTexture(), newX, newY);
            });
        });
    }

    private void updateBuildings(float delta) {
        for (Map.Entry<Island, GridPoint2> islandEntry : getVisibleIslands().entrySet()) {
            Island island = islandEntry.getKey();
            GridPoint2 islandLoc = islandEntry.getValue();

            for (Map.Entry<Entity<? extends BuildingEntityType<?>>, GridPoint2> buildEntry : island.getBuildings().entrySet()) {
                Entity<BuildingEntityType<?>> a;

                GridPoint2 buildingLocation = buildEntry.getValue();

                float relativeToFocusX = islandLoc.x + buildingLocation.x;
                float relativeToFocusY = islandLoc.y + buildingLocation.y;

                relativeToFocusX += entity.getType().getTileWidth() / 2f;
                relativeToFocusY += entity.getType().getTileHeight() / 2f;

                // To pixels
                relativeToFocusX *= 16;
                relativeToFocusY *= 16;

                int centerX = Math.round(relativeToFocusX);
                int centerY = Math.round(relativeToFocusY);

                entity.getType().check(entity, delta, getPlayer(), getMatchMap());
                entity.getType().present(entity, getBatch(), centerX, centerY);
            }
        }
    }

    private <T extends BuildingEntityType<T>> void handleBuildButton(BuildButton<T> buildButton, Rectangle focusedMapRectangle) {
        Texture associatedTexture = buildButton.getBuildingType().getTexture();

        float absoluteX = buildButton.getBuildPosition().x;
        float absoluteY = buildButton.getBuildPosition().y;

        // Above coords represent bottom left of build overlay. User wants to drag by center,
        // so adjust to that preference.
        float adjustedAbsoluteX = absoluteX - associatedTexture.getWidth() / 2;
        float adjustedAbsoluteY = absoluteY - associatedTexture.getHeight() / 2;

        getBatch().draw(associatedTexture, adjustedAbsoluteX, adjustedAbsoluteY);

        int xTilePosition = Math.round(focusedMapRectangle.getX() + adjustedAbsoluteX / 16);
        int yTilePosition = Math.round(focusedMapRectangle.getY() + adjustedAbsoluteY / 16);

        xTilePosition -= getMatchMap().getIslands().get(getPlayer().getMainIsland()).x;
        yTilePosition -= getMatchMap().getIslands().get(getPlayer().getMainIsland()).y;

        boolean canBuild = false;

        for (Island island : getVisibleIslands().keySet())
            if (island.canBuild(buildButton.getBuildingType(), xTilePosition, yTilePosition))
                canBuild = true;

        if (!buildButton.getBuildingType().canCharge(getPlayer().getInventory()))
            canBuild = false;

        System.out.println("reached");

        getBatch().draw(canBuild ? checkmark : x, adjustedAbsoluteX, adjustedAbsoluteY + associatedTexture.getHeight());

        if (canBuild && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            getPlayer().getMainIsland().build(buildButton.newBuilding(), xTilePosition, yTilePosition);

            buildButton.setBuildPosition(null);
            buildButton.getBuildingType().charge(getPlayer().getInventory());
        }
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
        getStage().getBatch().dispose();
    }

    public Game getGame() {
        return game;
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

    public Stage getStage() {
        return stage;
    }

    public Batch getBatch() {
        return batch;
    }

    public boolean isTouchDown() {
        return isTouchDown;
    }

    public void setTouchDown(boolean touchDown) {
        isTouchDown = touchDown;
    }

    public VerticalGroup getBuildMenu() {
        return buildMenu;
    }

    public Map<Island, GridPoint2> getVisibleIslands() {
        return visibleIslands;
    }

    public VerticalGroup getResourceMenu() {
        return resourceMenu;
    }

    public Map<Resource, Texture> getTreasureTextures() {
        return treasureTextures;
    }

    public Map<Resource, Texture> getResourceTextures() {
        return resourceTextures;
    }
}
