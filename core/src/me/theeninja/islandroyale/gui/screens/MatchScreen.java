package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.*;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.building.BuildingEntityType;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;
import me.theeninja.islandroyale.entity.building.ResourceBuildingType;
import me.theeninja.islandroyale.entity.controllable.PersonEntityType;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;
import me.theeninja.islandroyale.entity.controllable.TransportEntityType;

import java.util.*;

public class MatchScreen implements Screen {

    public final static Skin FLAT_EARTH_SKIN;

    private final List<BuildButton<?>> buildButtons = new ArrayList<>();

    static {
        FileHandle flatEarthSkinFileHandler = new FileHandle("flat-earth/skin/flat-earth-ui.json");
        FLAT_EARTH_SKIN = new Skin(flatEarthSkinFileHandler);
    }

    private final Game game;

    private final MatchMap matchMap;
    private final Player player;

    private final MapOverlay mapOverlay;

    private final Stage hudStage;
    private final Stage mapStage;

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

    private final MatchScreenInputListener inputListener;

    private <T extends EntityType<T>> List<T> getEntityTypes(String directory, Class<T> classType) {
        return new EntityTypeFactory<>(directory, classType).getEntityTypes();
    }

    private final List<EntityType<?>> entityTypes = new ArrayList<>();

    public <T extends BuildingEntityType<T>> List<T> getAndAddBuildingEntityTypes(String directory, Class<T> classType) {
        EntityTypeFactory<T> entityTypeFactory = new EntityTypeFactory<>(directory, classType);

        for (T buildingEntityType : entityTypeFactory.getEntityTypes())
            getBuildButtons().add(new BuildButton<>(buildingEntityType, player));

        return entityTypeFactory.getEntityTypes();
    }

    private final Camera mapCamera;
    private final Viewport mapViewport;
    private final Viewport screenViewport;

    private Viewport getMapViewport() {
        return mapViewport;
    }

    public MatchScreen(Game game) {
        entityTypes.addAll(getEntityTypes(EntityType.PERSON_DIRECTORY, PersonEntityType.class));
        entityTypes.addAll(getEntityTypes(EntityType.INTERACTABLE_PROJECTILE_DIRECTORY, InteractableProjectileEntityType.class));
        entityTypes.addAll(getEntityTypes(EntityType.STATIC_PROJECTILE_DIRECTORY, StaticProjectileEntityType.class));
        entityTypes.addAll(getEntityTypes(EntityType.TRANSPORT_DIRECTORY, TransportEntityType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.DEFENSE_DIRECTORY, DefenseBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.RESOURCE_DIRECTORY, ResourceBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.TRANSPORT_GENERATOR_DIRECTORY, TransportGeneratorBuildingType.class));
        entityTypes.addAll(getAndAddBuildingEntityTypes(EntityType.PERSON_GENERATOR_DIRECTORY, PersonGeneratorBuildingType.class));

        this.inputListener = new MatchScreenInputListener(this);

        this.batch = new SpriteBatch();

        this.game = game;

        this.mapCamera = new OrthographicCamera(160 / 2, 90 / 2);
        this.mapViewport = new StretchViewport(1000, 1000);

        this.screenViewport = new ScreenViewport();

        getMapCamera().position.set(getMapCamera().viewportWidth / 2, getMapCamera().viewportHeight / 2, 0);

        this.hudStage = new Stage(getScreenViewport(), getBatch());
        this.mapStage = new Stage(getMapViewport(), getBatch());

        this.matchMap = new MatchMap(1000, 1000);
        this.mapOverlay = new MapOverlay(getMatchMap(), this);

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

        Island chosenIsland = getMatchMap().getIslands().get(randomIslandNumber);

        this.player = new Player(chosenIsland);

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

        getHUDStage().addActor(getBuildMenu());
        getHUDStage().addActor(getResourceMenu());

        getPlayer().getInventory().put(Resource.WOOD, 1000);
    }

    private static boolean isBuildButtonInDragState(BuildButton buildButton) {
        return buildButton.getBuildPosition() != null;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getHUDStage());
    }

    private void drawIslands() {
        for (Island island : getMatchMap().getIslands())
            drawIsland(island);
    }
    private void drawIsland(Island island) {
        // Refers to position of island relative to bottom left of focus rectangle
        Vector2 islandCoords = island.getPositionOnMap().cpy();

        for (int islandTileX = 0; islandTileX < island.getMaxWidth(); islandTileX++) {
            for (int islandTileY = 0; islandTileY < island.getMaxHeight(); islandTileY++) {
                IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                // Do not go into statement if island tile type is water i.e null
                if (islandTileType != null) {
                    Texture islandTexture = getIslandTextures().get(islandTileType);

                    getBatch().draw(
                            islandTexture,
                            islandCoords.x + islandTileX,
                            islandCoords.y + islandTileY,
                            1,
                            1
                    );
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        getMapCamera().update();
        getBatch().setProjectionMatrix(getMapCamera().combined);

        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getBatch().begin();

        getVisibleIslands().clear();

        drawIslands();

        getMatchMap().flushDeadEntities();
        updateEntities(delta);
        moveEntities(delta);
        drawEntities();

        for (int i = 0; i < getBuildMenu().getChildren().size; i++) {
            Actor actor = getBuildMenu().getChildren().get(i);

            if (!(actor instanceof BuildButton))
                continue;

            BuildButton<?> buildButton = (BuildButton<?>) actor;

            if (!isBuildButtonInDragState(buildButton))
                continue;

            handleBuildButton(buildButton);
        }

        if (getInputListener().isMapShown())
            getMapOverlay().draw();

        drawLabelsAndUpdateResources(delta);

        presentEntities();

        getBatch().end();
        getHUDStage().draw();
        getMapStage().draw();
    }

    private void drawLabelsAndUpdateResources(float delta) {
        for (Actor actor : getResourceMenu().getChildren()) {
            if (!(actor instanceof ResourceLabel))
                continue;

            ResourceLabel resourceLabel = (ResourceLabel) actor;

            resourceLabel.update(delta);
        }
    }

    private void moveEntities(float delta) {
        for (Entity<? extends EntityType<?>> entity : getMatchMap().getEntities()) {
            float yDistance = (float) (Math.sin(entity.getVelocityPerSecond().y) * entity.getVelocityPerSecond().x);
            float xDistance = (float) (Math.cos(entity.getVelocityPerSecond().y) * entity.getVelocityPerSecond().x);

            entity.getSprite().translate(xDistance * delta, yDistance * delta);
        }
    }

    private void drawEntities() {
        for (Entity<? extends EntityType<?>> entity : getMatchMap().getEntities()) {
            System.out.println("Drawing entity of type " + entity.getType().getName() + " at " + entity.getSprite().getX() + " " + entity.getSprite().getY());
            entity.getSprite().draw(getBatch());
        }
    }

    private void presentEntities() {
        for (Entity<? extends EntityType<?>> entity : getMatchMap().getEntities()) {
            entity.present(getHUDStage());
        }
    }

    private void updateEntities(float delta) {
        List<Entity<? extends EntityType<?>>> entities = getMatchMap().getEntities();

        // I use an indexed for-loop here instead of a for-each loop because the entity check method might add
        // a new entity to the list (people shooting projectiles for instance). This allows me to avoid
        // concurrent modification exception.
        for (int i = 0; i < entities.size(); i++) {
            Entity<? extends EntityType<?>> entity = entities.get(i);
            entity.check(delta, getPlayer(), getMatchMap());
        }
    }

    public  <T extends EntityType<T>> void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        entity.getType().check(entity, delta, player, matchMap);
    }

    private <T extends BuildingEntityType<T>> void handleBuildButton(BuildButton<T> buildButton) {
        System.out.println("Handled");

        Texture associatedTexture = buildButton.getBuildingType().getTexture();

        Vector3 worldCoordsOfMosue = new Vector3(
                buildButton.getBuildPosition().x,
                // Mouse coords are relative to upper left, not bottom left, so make y
                // equal to height - y
                Gdx.graphics.getHeight() - buildButton.getBuildPosition().y,
                0
        );

        // Convert from screen coords to world coords
        getMapCamera().unproject(worldCoordsOfMosue);

        // Put mouse in center of buildign dragged, not bottom left
        worldCoordsOfMosue.x -= buildButton.getBuildingType().getTileWidth() / 2f;
        worldCoordsOfMosue.y -= buildButton.getBuildingType().getTileHeight() / 2f;

        getBatch().draw(associatedTexture, worldCoordsOfMosue.x, worldCoordsOfMosue.y, buildButton.getBuildingType().getTileWidth(), buildButton.getBuildingType().getTileHeight());

        boolean canBuild = false;

        int mouseTileXCoord = Math.round(worldCoordsOfMosue.x);
        int mouseTileYCoord = Math.round(worldCoordsOfMosue.y);

        for (Island island : getMatchMap().getIslands()) {
            Vector2 islandLocation = island.getPositionOnMap();

            int relativeToIslandX = (int) (mouseTileXCoord - islandLocation.x);
            int relativeToIslandY = (int) (mouseTileYCoord - islandLocation.y);

            if (island.canBuild(buildButton.getBuildingType(), relativeToIslandX, relativeToIslandY, getMatchMap()))
                canBuild = true;
        }

        //if (!buildButton.getBuildingType().canCharge(getPlayer().getInventory()))
        //    canBuild = false;

        getBatch().draw(canBuild ? checkmark : x, worldCoordsOfMosue.x, worldCoordsOfMosue.y + buildButton.getBuildingType().getTileHeight(), 1f, 1f);

        if (canBuild && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            Vector2 buildingLocation = new Vector2(
                    mouseTileXCoord,
                    mouseTileYCoord
            );

            Entity<? extends BuildingEntityType<?>> building = buildButton.newBuilding(buildingLocation);

            getMatchMap().getEntities().add(building);

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
        getHUDStage().getBatch().dispose();
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

    public Stage getHUDStage() {
        return hudStage;
    }

    public Stage getMapStage() {
        return mapStage;
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

    public List<BuildButton<?>> getBuildButtons() {
        return buildButtons;
    }

    public Camera getMapCamera() {
        return mapCamera;
    }

    public MapOverlay getMapOverlay() {
        return mapOverlay;
    }

    public MatchScreenInputListener getInputListener() {
        return inputListener;
    }

    public Viewport getScreenViewport() {
        return screenViewport;
    }
}
