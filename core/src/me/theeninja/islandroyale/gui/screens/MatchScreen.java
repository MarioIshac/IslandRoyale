package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.ai.HumanPlayer;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.building.BuildingType;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;
import me.theeninja.islandroyale.entity.building.ResourceGeneratorType;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectileType;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;
import me.theeninja.islandroyale.entity.controllable.PersonType;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;
import me.theeninja.islandroyale.entity.controllable.TransporterType;

import java.util.*;
import java.util.function.Consumer;

public class MatchScreen implements Screen {

    public final static int WHOLE_WORLD_TILE_WIDTH = 1000;
    public final static int WHOLE_WORLD_TILE_HEIGHT = 1000;

    public final static int VISIBLE_WORLD_TILE_WIDTH = 80;
    public final static int VISIBLE_WORLD_TILE_HEIGHT = 45;

    private final Array<BuildButton<?, ?>> buildButtons = new Array<>();

    private final Game game;

    private final MatchMap matchMap;
    private final me.theeninja.islandroyale.ai.Player player;

    private final Stage hudStage;
    private final Stage mapStage;

    private final Map<IslandTileType, Texture> islandTextures = new HashMap<>();
    private final Map<Resource, Texture> treasureTextures = new HashMap<>();
    private final Map<Resource, Texture> resourceTextures = new HashMap<>();
    private final Map<Island, Vector2> visibleIslands = new HashMap<>();

    private final Texture checkmark;
    private final Texture x;

    private final Batch batch;
    public final InputProcessor inputProcessor;

    private boolean isTouchDown;

    private final VerticalGroup buildMenu = new VerticalGroup();
    private final VerticalGroup resourceMenu = new VerticalGroup();

    private final MatchScreenInputListener inputListener;

    private <A extends Entity<A, B>, B extends EntityType<A, B>> void loadTypes(String directory, Class<B> classType, Consumer<B> consumer) {
        EntityTypeFactory<A, B> entityTypeFactory = new EntityTypeFactory<>(directory, classType);
        entityTypeFactory.loadEntityTypes(consumer);
    }

    private final Camera mapCamera;
    private final Camera hudCamera;
    private final Viewport mapViewport;
    private final Viewport hudViewport;

    public Viewport getMapViewport() {
        return mapViewport;
    }

    private <A extends Entity<A, B>, B extends EntityType<A, B>> void registerEntityType(B entityType) {
        EntityType.IDS.put(entityType.getId(), entityType);
    }

    private <A extends Building<A, B>, B extends BuildingType<A, B>> void registerBuildingType(B buildingEntityType, BuildingConstructor<A, B> buildingConstructor) {
        registerEntityType(buildingEntityType);

        BuildButton<A, B> buildButton = new BuildButton<A, B>(buildingEntityType, getPlayer(), buildingConstructor);
        getBuildButtons().add(buildButton);
    }

    private <A extends Building<A, B>, B extends BuildingType<A, B>> Consumer<B> getBuildingTypeRegisterer(BuildingConstructor<A, B> buildingConstructor) {
        return buildingEntityType -> registerBuildingType(buildingEntityType, buildingConstructor);
    }

    public MatchScreen(Game game) {
        // Non Buildings
        loadTypes(EntityType.PERSON_DIRECTORY, PersonType.class, this::registerEntityType);
        loadTypes(EntityType.INTERACTABLE_PROJECTILE_DIRECTORY, InteractableProjectileEntityType.class, this::registerEntityType);
        loadTypes(EntityType.DEFENSE_BULLET_PROJECTILE_DIRECTORY, DefenseBulletProjectileType.class, this::registerEntityType);
        loadTypes(EntityType.PERSON_BULLET_PROJECTILE_DIRECTORY, PersonBulletProjectileType.class, this::registerEntityType);
        loadTypes(EntityType.TRANSPORT_DIRECTORY, TransporterType.class, this::registerEntityType);

        // Buildings
        loadTypes(EntityType.DEFENSE_DIRECTORY, DefenseBuildingType.class, getBuildingTypeRegisterer(DefenseBuilding::new));
        loadTypes(EntityType.RESOURCE_DIRECTORY, ResourceGeneratorType.class, getBuildingTypeRegisterer(ResourceGenerator::new));
        loadTypes(EntityType.TRANSPORT_GENERATOR_DIRECTORY, TransporterGeneratorType.class, getBuildingTypeRegisterer(TransporterGenerator::new));
        loadTypes(EntityType.PERSON_GENERATOR_DIRECTORY, PersonGeneratorType.class, getBuildingTypeRegisterer(PersonGenerator::new));
        loadTypes(EntityType.PROJECTILE_GENERATOR_DIRECTORY, ProjectileGeneratorType.class, getBuildingTypeRegisterer(ProjectileGenerator::new));

        this.inputListener = new MatchScreenInputListener(this);

        this.batch = new SpriteBatch();

        this.game = game;

        this.mapCamera = new OrthographicCamera();
        this.mapViewport = new StretchViewport(VISIBLE_WORLD_TILE_WIDTH, VISIBLE_WORLD_TILE_HEIGHT, getMapCamera());

        this.hudCamera = new OrthographicCamera();
        this.hudViewport = new ScreenViewport(getHudCamera());

        this.mapStage = new Stage(getMapViewport(), getBatch());
        this.hudStage = new Stage(getHudViewport(), getBatch());

        this.matchMap = new MatchMap(WHOLE_WORLD_TILE_WIDTH, WHOLE_WORLD_TILE_HEIGHT);

        FileHandle checkMarkFileHandle = Gdx.files.internal("CheckMark.png");
        FileHandle xFileHandle = Gdx.files.internal("X.png");

        this.checkmark = new Texture(checkMarkFileHandle);
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

        this.player = new HumanPlayer(chosenIsland);

        getResourceMenu().space(20f);
        getBuildMenu().space(20f);

        for (BuildButton<?, ?> buildButton : getBuildButtons()) {
            getBuildMenu().addActor(buildButton);
        }

        for (Resource resource : Resource.values()) {
            FileHandle fileHandle = Gdx.files.internal("resource/" + resource.name() + ".png");
            Texture texture = new Texture(fileHandle);
            getResourceTextures().put(resource, texture);

            ResourceLabel resourceLabel = new ResourceLabel(resource, getResourceTextures().get(resource), getPlayer());
            getResourceMenu().addActor(resourceLabel);
        }

        getBuildMenu().setPosition(getHudViewport().getWorldWidth() - 100, getHudViewport().getWorldHeight() - 20);
        getResourceMenu().setPosition(100, getHudViewport().getWorldHeight() - 20);

        getHUDStage().addActor(getBuildMenu());
        getHUDStage().addActor(getResourceMenu());

        getPlayer().getInventory().put(Resource.WOOD, 1000);

        this.inputProcessor = new InputMultiplexer(
                getInputListener(),
                getHUDStage(),
                getMapStage()
        );
    }

    private static boolean isBuildButtonInDragState(BuildButton buildButton) {
        return buildButton.getBuildPosition() != null;
    }

    @Override
    public void show() {
        System.out.println("A");
        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void drawIslands() {
        System.out.println("A");
        for (Island island : getMatchMap().getIslands())
            drawIsland(island);
    }
    private void drawIsland(Island island) {
        // Refers to position of island relative to bottom left of focus rectangle
        for (int islandTileX = 0; islandTileX < island.getMaxWidth(); islandTileX++) {
            for (int islandTileY = 0; islandTileY < island.getMaxHeight(); islandTileY++) {
                IslandTileType islandTileType = island.getRepr()[islandTileX][islandTileY];

                // Do not go into statement if island tile type is water i.e null
                if (islandTileType != null) {
                    Texture islandTexture = getIslandTextures().get(islandTileType);

                    getBatch().draw(
                            islandTexture,
                            island.x + islandTileX,
                            island.y + islandTileY,
                            1,
                            1
                    );
                }
            }
        }
    }

    private void handleMapRendering() {
        getMapCamera().update();
        getBatch().setProjectionMatrix(getMapCamera().combined);

        drawIslands();
        presentEntities();

        for (BuildButton<?, ?> buildButton : getBuildButtons()) {
            if (!isBuildButtonInDragState(buildButton))
                continue;

            handleBuildButton(buildButton);
        }
    }

    private void handleHUDRendering(float delta) {
        getHudCamera().update();
        getBatch().setProjectionMatrix(getHudCamera().combined);

        drawLabelsAndUpdateResources(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //if (getInputListener().isMapShown())
        //    getMapViewport().setWorldSize(WHOLE_WORLD_TILE_WIDTH, WHOLE_WORLD_TILE_HEIGHT);
        //else
        //    getMapViewport().setWorldSize(VISIBLE_WORLD_TILE_WIDTH, VISIBLE_WORLD_TILE_HEIGHT);

        getMapViewport().apply(true);

        getMatchMap().flushDeadEntities();
        getMapStage().getRoot().clearChildren();

        for (Entity<?, ?> entity : getMatchMap().getEntities())
            getMapStage().addActor(entity);

        getBatch().begin();

        updateEntities(delta);
        moveEntities(delta);

        handleHUDRendering(delta);
        handleMapRendering();
        getBatch().end();
        getMapStage().act(delta);
        getMapStage().draw();
        getHUDStage().act(delta);
        getHUDStage().draw();
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
        for (Entity<?, ?> entity : getMatchMap().getEntities()) {
            float yDistance = (float) (Math.sin(entity.getVelocityPerSecond().y) * entity.getVelocityPerSecond().x);
            float xDistance = (float) (Math.cos(entity.getVelocityPerSecond().y) * entity.getVelocityPerSecond().x);

            entity.getSprite().translate(xDistance * delta, yDistance * delta);
        }
    }

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private void presentEntities() {
        for (Entity<?, ?> entity : getMatchMap().getEntities()) {
            entity.present(getMapCamera(), getHUDStage(), getShapeRenderer());
        }
    }

    private void updateEntities(float delta) {
        Array<Entity<?, ?>> entities = getMatchMap().getEntities();

        // I use an indexed for-loop here instead of a for-each loop because the entity check method might add
        // a new entity to the list (people shooting projectiles for instance). This allows me to avoid
        // concurrent modification exception.
        for (int i = 0; i < entities.size; i++) {
            Entity<?, ?> entity = entities.get(i);
            entity.check(delta, getPlayer(), getMatchMap());
        }
    }

    private <A extends Building<A, B>, B extends BuildingType<A, B>> void handleBuildButton(BuildButton<A, B> buildButton) {
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
            int relativeToIslandX = (int) (mouseTileXCoord - island.x);
            int relativeToIslandY = (int) (mouseTileYCoord - island.y);

            if (island.canBuild(buildButton.getBuildingType(), relativeToIslandX, relativeToIslandY, getMatchMap()))
                canBuild = true;
        }

        //if (!buildButton.getBuildingType().canCharge(getPlayer().getInventory()))
        //    canBuild = false;

        getBatch().draw(canBuild ? checkmark : x, worldCoordsOfMosue.x, worldCoordsOfMosue.y + buildButton.getBuildingType().getTileHeight(), 1f, 1f);

        if (canBuild && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            Building<A, B> building = buildButton.newBuilding(mouseTileXCoord, mouseTileYCoord);

            getMatchMap().getEntities().add(building);

            buildButton.setBuildPosition(null);
            //buildButton.getBuildingType().charge(getPlayer().getInventory());
        }
    }

    @Override
    public void resize(int width, int height) {
        getHudViewport().update(width, height, true);
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

    public Array<BuildButton<?, ?>> getBuildButtons() {
        return buildButtons;
    }

    public Camera getMapCamera() {
        return mapCamera;
    }

    public MatchScreenInputListener getInputListener() {
        return inputListener;
    }

    public Viewport getHudViewport() {
        return hudViewport;
    }

    public Camera getHudCamera() {
        return hudCamera;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}
