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
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.building.BuildingEntityType;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;
import me.theeninja.islandroyale.entity.building.ResourceBuildingType;
import me.theeninja.islandroyale.entity.controllable.PersonEntityType;
import me.theeninja.islandroyale.entity.controllable.ProjectileEntityType;
import me.theeninja.islandroyale.entity.controllable.TransportEntityType;

import java.util.*;

public class MatchScreen implements Screen {

    public final static Skin FLAT_EARTH_SKIN;

    private final Array<BuildButton<?>> buildButtons = new Array<>();

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
    private final Map<Island, Vector2> visibleIslands = new HashMap<>();

    private final Texture checkmark;
    private final Texture x;

    private final Batch batch;

    private boolean isTouchDown;

    private final VerticalGroup buildMenu = new VerticalGroup();
    private final VerticalGroup resourceMenu = new VerticalGroup();

    private <T extends EntityType<T>> Array<T> getEntityTypes(String directory, Class<T> classType) {
        return new EntityTypeFactory<>(directory, classType).getEntityTypes();
    }

    private final Array<EntityType<?>> entityTypes = new Array<>();

    public <T extends BuildingEntityType<T>> Array<T> getAndAddBuildingEntityTypes(String directory, Class<T> classType) {
        EntityTypeFactory<T> entityTypeFactory = new EntityTypeFactory<>(directory, classType);

        for (T buildingEntityType : entityTypeFactory.getEntityTypes()) {
            getBuildButtons().add(new BuildButton<>(buildingEntityType, player));
        }

        return entityTypeFactory.getEntityTypes();
    }


    public MatchScreen(Game game) {
        entityTypes.addAll(getEntityTypes(EntityType.PERSON_DIRECTORY, PersonEntityType.class));
        entityTypes.addAll(getEntityTypes(EntityType.PROJECTILE_DIRECTORY, ProjectileEntityType.class));
        entityTypes.addAll(getEntityTypes(EntityType.TRANSPORT_DIRECTORY, TransportEntityType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.DEFENSE_DIRECTORY, DefenseBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.RESOURCE_DIRECTORY, ResourceBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.OFFENSE_DIRECTORY, PersonGeneratorBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.OFFENSE_DIRECTORY, TransportGeneratorBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.OFFENSE_DIRECTORY, ProjectileGeneratorBuildingType.class));

        this.batch = new SpriteBatch();

        this.game = game;

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

        Vector2 bottomLeftFocusPos = this.getMatchMap().getIslands().get(chosenIsland).cpy();

        bottomLeftFocusPos.sub(20, 20);

        getMatchMap().getFocusOrigin().set(bottomLeftFocusPos);

        getResourceMenu().space(20f);
        getBuildMenu().space(20f);

        for (BuildButton<?> buildButton : getBuildButtons()) {
            getBuildMenu().addActor(buildButton);
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

    private boolean isOutOfFocus(Vector2 relativeTileLocation) {
        float screenTileWidth = Gdx.graphics.getWidth() / 16f;
        float screenTileHeight = Gdx.graphics.getHeight() / 16f;

        boolean xInBounds = 0 < relativeTileLocation.x && relativeTileLocation.x < screenTileHeight;
        boolean yInBounds = 0 < relativeTileLocation.y && relativeTileLocation.y < screenTileHeight;

        return !xInBounds || !yInBounds;
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
        Vector2 islandCoordsRelativeToFocus = getMatchMap().getIslands().get(island)
                // Must be copied since we do not want to modify position, it should be immutable
                .cpy();

        // Changes coord position from absolute to relative to focus
        islandCoordsRelativeToFocus.sub(getMatchMap().getFocusOrigin());

        getVisibleIslands().put(island, islandCoordsRelativeToFocus);

        for (int islandTileX = 0; islandTileX < island.getMaxWidth(); islandTileX++) {
            for (int islandTileY = 0; islandTileY < island.getMaxHeight(); islandTileY++) {
                // These refer to the position that each tile of the island has
                // relative to the bottom left of the focused part of the map.
                float relativeXToFocusPoint = islandCoordsRelativeToFocus.x + islandTileX;
                float relativeYToFocusPoint = islandCoordsRelativeToFocus.y + islandTileY;

                // Though we have verified that some part of the island is within the
                // focused part of the map, it is is still possible that SOME of the island's tiles
                // are NOT in focus.
                boolean isXOutOfFocus = relativeXToFocusPoint < 0;
                boolean isYOutOfFocus = relativeYToFocusPoint < 0;

                // No need to render these said tiles of the island
                if (isXOutOfFocus || isYOutOfFocus)
                    continue;

                IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                float relativeCoordXToFocusPoint = relativeXToFocusPoint * 16;
                float relativeCoordYToFocusPoint = relativeYToFocusPoint * 16;

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

        getMatchMap().flushDeadEntities();
        updateEntities(delta);
        drawEntities();

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
        presentEntities();
        getStage().draw();
    }

    private void drawEntities() {
        for (Map.Entry<Entity<? extends EntityType<?>>, Vector2> entry : getMatchMap().getEntities().entrySet()) {
            Entity<? extends EntityType<?>> entity = entry.getKey();
            Vector2 entityLocation = entry.getValue();

            Vector2 relativeEntityLocation = getMatchMap().absoluteToRelativeTile(entityLocation);

            if (isOutOfFocus(relativeEntityLocation))
                continue;

            int relativePixelX = (int) (relativeEntityLocation.x * 16);
            int relativePixelY = (int) (relativeEntityLocation.y * 16);

            getBatch().draw(entity.getType().getTexture(), relativePixelX, relativePixelY);
        }
    }

    private void presentEntities() {
        for (Map.Entry<Entity<? extends EntityType<?>>, Vector2> entry : getMatchMap().getEntities().entrySet()) {
            Entity<? extends EntityType<?>> entity = entry.getKey();
            Vector2 entityLocation = entry.getValue();

            Vector2 relativeEntityLocation = getMatchMap().absoluteToRelativeTile(entityLocation);

            if (isOutOfFocus(relativeEntityLocation))
                continue;

            entity.present(getBatch(), relativeEntityLocation.x, relativeEntityLocation.y);
        }
    }

    private void updateEntities(float delta) {
        for (Map.Entry<Entity<? extends EntityType<?>>, Vector2> entry : getMatchMap().getEntities().entrySet()) {
            Entity<? extends EntityType<?>> entity = entry.getKey();
            Vector2 entityLocation = entry.getValue();
            Vector2 relativeEntityLocation = getMatchMap().absoluteToRelativeTile(entityLocation);

            entity.check(delta, getPlayer(), getMatchMap());
        }
    }

    public  <T extends EntityType<T>> void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        entity.getType().check(entity, delta, player, matchMap);
    }

    public  <T extends EntityType<T>> void present(Entity<T> entity, Batch batch, float tileX, float tileY) {
        entity.getType().present(entity, batch, tileX, tileY);
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

        int absoluteXBuildPos = Math.round(focusedMapRectangle.getX() + adjustedAbsoluteX / 16);
        int absoluteYBuildPos = Math.round(focusedMapRectangle.getY() + adjustedAbsoluteY / 16);

        boolean canBuild = false;

        for (Island island : getVisibleIslands().keySet()) {
            Vector2 islandLocation = getMatchMap().getIslands().get(island);

            int relativeToIslandX = (int) (absoluteXBuildPos - islandLocation.x);
            int relativeToIslandY = (int) (absoluteYBuildPos - islandLocation.y);

            if (island.canBuild(buildButton.getBuildingType(), relativeToIslandX, relativeToIslandY, getMatchMap()))
                canBuild = true;
        }

        //if (!buildButton.getBuildingType().canCharge(getPlayer().getInventory()))
        //    canBuild = false;

        getBatch().draw(canBuild ? checkmark : x, adjustedAbsoluteX, adjustedAbsoluteY + associatedTexture.getHeight());

        if (canBuild && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            Entity<?> building = buildButton.newBuilding(new Vector2(adjustedAbsoluteX, adjustedAbsoluteY));

            Vector2 absoluteBuildPos = new Vector2(absoluteXBuildPos, absoluteYBuildPos);

            getMatchMap().getEntities().put(building, absoluteBuildPos);

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

    public Map<Island, Vector2> getVisibleIslands() {
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

    public Array<BuildButton<?>> getBuildButtons() {
        return buildButtons;
    }
}
