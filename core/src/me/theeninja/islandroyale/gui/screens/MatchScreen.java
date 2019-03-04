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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.ai.AIPlayer;
import me.theeninja.islandroyale.ai.HumanPlayer;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.building.BuildingType;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;
import me.theeninja.islandroyale.entity.building.ResourceGeneratorType;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectileType;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;
import me.theeninja.islandroyale.entity.controllable.*;
import me.theeninja.islandroyale.entity.treasure.*;

import java.util.*;

public class MatchScreen implements Screen {
    public final static int WHOLE_WORLD_TILE_WIDTH = 1000;
    public final static int WHOLE_WORLD_TILE_HEIGHT = 1000;

    public final static int VISIBLE_WORLD_TILE_WIDTH = 80;
    public final static int VISIBLE_WORLD_TILE_HEIGHT = 46;

    private final Array<BuildButton<?, ?>> buildButtons = new Array<>();

    private final Game game;

    private final Stage hudStage;
    private final Stage mapStage;

    private final Match match;

    private final Texture[] islandTextures = new Texture[IslandTileType.TILE_COUNT];
    private final Map<Resource, Texture> treasureTextures = new HashMap<>();
    private final Map<Resource, Texture> resourceTextures = new HashMap<>();

    private final Texture checkmark;
    private final Texture x;

    private final Batch batch;
    private final InputProcessor inputProcessor;
    private final Player player;

    private boolean isTouchDown;

    private final VerticalGroup buildMenu = new VerticalGroup();
    private final VerticalGroup resourceMenu = new VerticalGroup();

    private final MatchScreenInputListener inputListener;

    private final Camera mapCamera;
    private final Camera hudCamera;
    private final Viewport mapViewport;
    private final Viewport hudViewport;

    public Viewport getMapViewport() {
        return mapViewport;
    }

    private final Button cycleToPreviousHeadQuartersButton = new TextButton("<", Skins.getInstance().getFlatEarthSkin());
    private final Button cycleToNextHeadQuartersButton = new TextButton(">", Skins.getInstance().getFlatEarthSkin());

    public Button getCycleToNextHeadQuartersButton() {
        return cycleToNextHeadQuartersButton;
    }

    public MatchScreen(Game game) {
        EntityTypeManager entityTypeManager = new EntityTypeManager();

        Array<ResourceTreasureType> resourceTreasureTypes = new Array<>();
        Array<DataTreasureType> dataTreasureTypes = new Array<>();
        Array<HeadQuartersType> headQuartersTypes = new Array<>();

        EntityTypeRegisterer<?, ?>[] entityTypeRegisterers = {
            new EntityTypeRegisterer<>(entityTypeManager, PersonType.class, EntityType.PERSON_DIRECTORY),
            new EntityTypeRegisterer<>(entityTypeManager, DefenseBulletProjectileType.class, EntityType.DEFENSE_BULLET_PROJECTILE_DIRECTORY),
            new EntityTypeRegisterer<>(entityTypeManager, PersonBulletProjectileType.class, EntityType.PERSON_BULLET_PROJECTILE_DIRECTORY),
            new EntityTypeRegisterer<>(entityTypeManager, TransporterType.class, EntityType.TRANSPORT_DIRECTORY),
            new BuildingTypeRegisterer<>(entityTypeManager, getBuildButtons(), DefenseBuildingType.class, getPlayer(), DefenseBuilding::new, EntityType.DEFENSE_DIRECTORY),
            new BuildingTypeRegisterer<>(entityTypeManager, getBuildButtons(), ResourceGeneratorType.class, getPlayer(), ResourceGenerator::new, EntityType.RESOURCE_DIRECTORY),
            new BuildingTypeRegisterer<>(entityTypeManager, getBuildButtons(), TransporterGeneratorType.class, getPlayer(), TransporterGenerator::new, EntityType.TRANSPORT_GENERATOR_DIRECTORY),
            new BuildingTypeRegisterer<>(entityTypeManager, getBuildButtons(), PersonGeneratorType.class, getPlayer(), PersonGenerator::new, EntityType.PERSON_GENERATOR_DIRECTORY),
            new BuildingTypeRegisterer<>(entityTypeManager, getBuildButtons(), ProjectileGeneratorType.class, getPlayer(), ProjectileGenerator::new, EntityType.PROJECTILE_GENERATOR_DIRECTORY),
            new TreasureTypeRegisterer<>(entityTypeManager, resourceTreasureTypes, ResourceTreasureType.class, EntityType.RESOURCE_TREASURE_DIRECTORY),
            new TreasureTypeRegisterer<>(entityTypeManager, dataTreasureTypes, DataTreasureType.class, EntityType.DATA_TREASURE_DIRECTORY),
            new HeadQuartersTyoeRegisterer(entityTypeManager, headQuartersTypes, HeadQuartersType.class, EntityType.HEADQUARTERS_DIRECTORY),
            new EntityTypeRegisterer<>(entityTypeManager, InteractableProjectileEntityType.class, EntityType.INTERACTABLE_PROJECTILE_DIRECTORY)
        };

        for (EntityTypeRegisterer<?, ?> entityTypeRegisterer : entityTypeRegisterers) {
            entityTypeRegisterer.registerEntityTypeDirectory();
        }

        this.inputListener = new MatchScreenInputListener(this);

        this.batch = new SpriteBatch();

        this.game = game;

        this.mapCamera = new OrthographicCamera();
        this.mapViewport = new StretchViewport(VISIBLE_WORLD_TILE_WIDTH, VISIBLE_WORLD_TILE_HEIGHT, getMapCamera());

        this.hudCamera = new OrthographicCamera();
        this.hudViewport = new ScreenViewport(getHUDCamera());

        this.mapStage = new Stage(getMapViewport(), getBatch());
        this.hudStage = new Stage(getHUDViewport(), getBatch());

        Player[] players = {
            new HumanPlayer("Mario"),
            new AIPlayer(1, "Hola")
        };

        MatchMap matchMap = new MatchMap(
            WHOLE_WORLD_TILE_WIDTH,
            WHOLE_WORLD_TILE_HEIGHT,
            this,
            dataTreasureTypes,
            resourceTreasureTypes,
            headQuartersTypes,
            players,
            MapMode.NEUTRAL
        );

        this.match = new Match(matchMap, entityTypeManager);

        FileHandle checkMarkFileHandle = Gdx.files.internal("CheckMark.png");
        FileHandle xFileHandle = Gdx.files.internal("X.png");

        this.checkmark = new Texture(checkMarkFileHandle);
        this.x = new Texture(xFileHandle);

        for (byte islandTileType = 0; islandTileType <= IslandTileType.END_TILE; islandTileType++) {
            final String texturePath = IslandTileType.TEXTURE_PATHS[islandTileType];

            if (texturePath == null)
                continue;

            final FileHandle textureFileHandler = Gdx.files.internal(texturePath);
            final Texture texture = new Texture(textureFileHandler);

            getIslandTextures()[islandTileType] = texture;
        }

        this.player = players[0];

        getPlayer().setHeadQuartersIndex(0);

        getResourceMenu().space(20f);
        getBuildMenu().space(20f);

        for (BuildButton<?, ?> buildButton : getBuildButtons())
            getBuildMenu().addActor(buildButton);

        for (Resource resource : Resource.values()) {
            final FileHandle fileHandle = Gdx.files.internal("resource/" + resource.name() + ".png");
            final Texture texture = new Texture(fileHandle);
            getResourceTextures().put(resource, texture);

            final ResourceLabel resourceLabel = new ResourceLabel(resource, getResourceTextures().get(resource), getPlayer());
            getResourceMenu().addActor(resourceLabel);
        }

        getBuildMenu().setPosition(getHUDViewport().getWorldWidth() - 100, getHUDViewport().getWorldHeight() - 20);
        getResourceMenu().setPosition(100, getHUDViewport().getWorldHeight() - 20);

        getHUDStage().addActor(getBuildMenu());
        getHUDStage().addActor(getResourceMenu());

        getPlayer().getInventory().put(Resource.WOOD, 1000);

        this.inputProcessor = new InputMultiplexer(
            getHUDStage(),
            getInputListener(),
            getMapStage()
        );

        getHUDStage().addActor(getCycleToPreviousHeadQuartersButton());
        getHUDStage().addActor(getCycleToNextHeadQuartersButton());

        getCycleToNextHeadQuartersButton().setPosition(
                Gdx.graphics.getWidth() - getCycleToNextHeadQuartersButton().getWidth() - 20,
                20
        );

        getCycleToPreviousHeadQuartersButton().setPosition(
                getCycleToNextHeadQuartersButton().getX() - getCycleToPreviousHeadQuartersButton().getWidth() - 20,
                20
        );

        InputListener cycleToPreviousHeadQuartersListener = new HeadquartersCycleListener(getPlayer(), -1);
        InputListener cycleToNextHeadQuartersListener = new HeadquartersCycleListener(getPlayer(), 1);

        getCycleToPreviousHeadQuartersButton().addListener(cycleToPreviousHeadQuartersListener);
        getCycleToNextHeadQuartersButton().addListener(cycleToNextHeadQuartersListener);
    }

    private void toggleHeadQuarterCycleButtons() {
        boolean headQuartersAvailableToCycleTo = getPlayer().getAllHeadQuarters().size != 1;

        getCycleToPreviousHeadQuartersButton().setDisabled(headQuartersAvailableToCycleTo);
        getCycleToNextHeadQuartersButton().setDisabled(headQuartersAvailableToCycleTo);
    }

    private static boolean isBuildButtonInDragState(BuildButton buildButton) {
        return buildButton.getBuildPosition() != null;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getInputProcessor());
    }

    private void updateMapCamera() {
        if (PathSelectionInputListener.areAnyInUse()) {
            getMapViewport().setWorldSize(WHOLE_WORLD_TILE_WIDTH, WHOLE_WORLD_TILE_HEIGHT);

            getMapCamera().position.set(
                WHOLE_WORLD_TILE_WIDTH / 2f,
                WHOLE_WORLD_TILE_HEIGHT / 2f,
                0
            );
        } else {
            getMapViewport().setWorldSize(VISIBLE_WORLD_TILE_WIDTH, VISIBLE_WORLD_TILE_HEIGHT);

            getMapCamera().position.set(
                getPlayer().getX() + VISIBLE_WORLD_TILE_WIDTH / 2f,
                getPlayer().getY() + VISIBLE_WORLD_TILE_HEIGHT / 2f,
                0
            );
        }

        getMapViewport().apply(false);
    }

    private void drawWorld() {
        final boolean inUse = PathSelectionInputListener.areAnyInUse();

        for (int islandTileXOffset = 0; islandTileXOffset < getMapViewport().getWorldWidth(); islandTileXOffset++) {
            final int islandTileX = inUse ? islandTileXOffset : (getPlayer().getX() + islandTileXOffset) % WHOLE_WORLD_TILE_WIDTH;

            for (int islandTileYOffset = 0; islandTileYOffset < getMapViewport().getWorldHeight(); islandTileYOffset++) {
                final int islandTileY = inUse ? islandTileYOffset : (getPlayer().getY() + islandTileYOffset) % WHOLE_WORLD_TILE_HEIGHT;

                final byte islandTileType = getMatch().getMatchMap().getTiles()[islandTileX][islandTileY];

                // Don't draw detailed ocean in map rendering
                if (inUse && !IslandTileType.isGround(islandTileType))
                    continue;

                final Texture islandTexture = getIslandTextures()[islandTileType];

                getBatch().draw(
                    islandTexture,
                    islandTileX,
                    islandTileY,
                    1,
                    1
                );
            }
        }
    }

    private void handleMapRendering() {
        getMapCamera().update();
        getBatch().setProjectionMatrix(getMapCamera().combined);

        drawWorld();
        presentEntities();

        for (final BuildButton<?, ?> buildButton : getBuildButtons()) {
            if (isBuildButtonInDragState(buildButton)) {
                handleBuildButton(buildButton);
            }
        }
    }

    private void handleHUDRendering(float delta) {
        getHUDCamera().update();
        getBatch().setProjectionMatrix(getHUDCamera().combined);

        drawLabelsAndUpdateResources(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
            IslandTileType.OCEAN_TILE_R_NORMALIZED,
            IslandTileType.OCEAN_TILE_G_NORMALIZED,
            IslandTileType.OCEAN_TILE_B_NORMALIZED,
            1
        );

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        for (Player player : getMatch().getMatchMap().getPlayers()) {
            player.update(getMatch());
        }

        updateMapCamera();
        getMatch().getMatchMap().flushDeadEntities();
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

        toggleHeadQuarterCycleButtons();
    }

    private void drawLabelsAndUpdateResources(float delta) {
        for (final Actor actor : getResourceMenu().getChildren()) {
            if (!(actor instanceof ResourceLabel))
                continue;

            final ResourceLabel resourceLabel = (ResourceLabel) actor;

            resourceLabel.update(delta);
        }
    }

    private void moveEntities(float delta) {
        for (int entityTypeKey = 0; entityTypeKey < EntityType.Unsafe.ENTITY_TYPE_CLASS_INDICES.length; entityTypeKey++) {
            final Array<Entity<?, ?>> priorityEntities = getMatch().getMatchMap().getEntities()[entityTypeKey];

            for (final Entity<?, ?> entity : priorityEntities) {
                final float yDistance = (float) (Math.sin(entity.getDirection()) * entity.getSpeed());
                final float xDistance = (float) (Math.cos(entity.getDirection()) * entity.getSpeed());

                entity.setPosition(
                    entity.getX() + xDistance * delta,
                    entity.getY() + yDistance * delta
                );
            }
        }


    }

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private void presentEntities() {
        for (int entityTypeIndex = 0; entityTypeIndex < getMatch().getMatchMap().getEntities().length; entityTypeIndex++) {
            final Array<Entity<?, ?>> priorityEntities = getMatch().getMatchMap().getEntities()[entityTypeIndex];

            for (Entity<?, ?> entity : priorityEntities)
                entity.present(getMapCamera(), getHUDStage(), getShapeRenderer());
        }
    }

    private void updateEntities(float delta) {
        for (int entityTypeIndex = 0; entityTypeIndex < getMatch().getMatchMap().getEntities().length; entityTypeIndex++) {
            final Array<Entity<?, ?>> priorityEntities = getMatch().getMatchMap().getEntities()[entityTypeIndex];

            // I use an indexed for-loop here instead of a for-each loop because the entity check method might add
            // a new entity to the list (people shooting projectiles for instance). This allows me to avoid
            // concurrent modification exception.
            for (int i = 0; i < priorityEntities.size; i++) {
                final Entity<?, ?> entity = priorityEntities.get(i);
                entity.check(delta, getPlayer(), getMatch());
            }
        }
    }

    private <A extends Building<A, B>, B extends BuildingType<A, B>> void handleBuildButton(BuildButton<A, B> buildButton) {
        Texture associatedTexture = buildButton.getBuildingType().getTexture();

        final float worldX = buildButton.getBuildPosition().x / Gdx.graphics.getWidth() * VISIBLE_WORLD_TILE_WIDTH + getPlayer().getX()
                - buildButton.getBuildingType().getTileWidth() / 2f;

        final float worldY = buildButton.getBuildPosition().y / Gdx.graphics.getHeight() * VISIBLE_WORLD_TILE_HEIGHT + getPlayer().getY()
                - buildButton.getBuildingType().getTileHeight() / 2f;

        System.out.println("World Coords of Mouse " + worldX + " " + worldY);
        System.out.println("Player " + getPlayer().getX() + " " + getPlayer().getY());
        System.out.println("Match Screen " + getMapCamera().position.x + " " + getMapCamera().position.y);

        final int roundedWorldX = Math.round(worldX);
        final int roundedWorldY = Math.round(worldY);

        getBatch().draw(associatedTexture, roundedWorldX, roundedWorldY, buildButton.getBuildingType().getTileWidth(), buildButton.getBuildingType().getTileHeight());

        final B buildingType = buildButton.getBuildingType();

        final Inventory requiredCost = buildButton.getBuildingType().getInventoryCost();

        final boolean canBuild =
            // Can Afford
            getPlayer().getInventory().has(requiredCost) &&

            // Can Place
            getMatch().getMatchMap().canBuild(buildingType, roundedWorldX, roundedWorldY);

        final float checkMarkX = roundedWorldX + buildButton.getBuildingType().getTileWidth() / 2f;
        final float checkmarkY = roundedWorldY + buildButton.getBuildingType().getTileHeight();

        final Texture canBuildIndication = canBuild ? checkmark : x;

        getBatch().draw(canBuildIndication, checkMarkX, checkmarkY, 1f, 1f);

        if (canBuild && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            final A building = buildButton.newBuilding(roundedWorldX, roundedWorldY, getMatch());

            getMatch().getMatchMap().addEntitySafely(building);

            buildButton.setBuildPosition(null);
            //buildButton.getBuildingType().charge(getPlayer().getInventory());
        }
    }

    @Override
    public void resize(int width, int height) {
        getHUDViewport().update(width, height, true);
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
    
    public Texture[] getIslandTextures() {
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

    public Viewport getHUDViewport() {
        return hudViewport;
    }

    public Camera getHUDCamera() {
        return hudCamera;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public Match getMatch() {
        return match;
    }

    public Button getCycleToPreviousHeadQuartersButton() {
        return cycleToPreviousHeadQuartersButton;
    }
}
