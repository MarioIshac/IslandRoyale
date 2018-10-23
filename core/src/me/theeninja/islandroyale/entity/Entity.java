package me.theeninja.islandroyale.entity;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.gui.screens.Match;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public abstract class Entity<A extends Entity<A, B, C>, B extends EntityType<A, B, C>, C extends EntityBrand<A, B, C>> extends Actor {
    public static float rangeBetweenSquared(Entity<?, ?> entityOne, Entity<?, ?> entityTwo) {
        final float insideXDiff = entityOne.getSprite().getX() - entityTwo.getSprite().getY();
        final float insideYDiff = entityOne.getSprite().getY() - entityTwo.getSprite().getY();

        final float outsideXDiff = MatchScreen.WHOLE_WORLD_TILE_WIDTH - insideXDiff;
        final float outsideYDiff = MatchScreen.WHOLE_WORLD_TILE_HEIGHT - insideYDiff;

        final float insideDistanceSquared = insideXDiff * insideXDiff + insideYDiff * insideYDiff;
        final float outsideDistanceSquared = outsideXDiff * outsideXDiff + outsideYDiff * outsideYDiff;

        return Math.max(insideDistanceSquared, outsideDistanceSquared);
    }

    /**
     * Performs check outlined in <a href="https://stackoverflow.com/questions/23302698/java-check-if-two-rectangles-overlap-at-any-point">here</a>.
     *
     * @return Whether the rectangles defined by the provided coordinates overlap. Do note that this is different than contains (one rectangle
     * must only partially include another rectangle, not completely).
     */
    protected static boolean overlap(Entity<?, ?> entityOne, Entity<?, ?> entityTwo) {
        float bottomXEntityOne = (entityOne.getSprite().getX());
        float bottomYEntityOne = (entityOne.getSprite().getY());

        float upperXEntityOne = bottomXEntityOne + entityOne.getSprite().getWidth();
        float upperYEntityOne = bottomYEntityOne + entityOne.getSprite().getHeight();

        float bottomXEntityTwo = (entityTwo.getSprite().getX());
        float bottomYEntityTwo = (entityTwo.getSprite().getY());

        float upperXEntityTwo = bottomXEntityTwo + entityTwo.getSprite().getWidth();
        float upperYEntityTwo = bottomYEntityTwo + entityTwo.getSprite().getHeight();

        boolean toRight = bottomXEntityTwo > upperXEntityOne;
        boolean toUp = bottomYEntityTwo > upperYEntityOne;
        boolean toLeft = upperXEntityTwo < bottomXEntityOne;
        boolean toDown = upperYEntityTwo < bottomYEntityOne;

        boolean doesNotOverlap = toRight || toUp || toLeft || toDown;

        return !doesNotOverlap;
    }

    private static final String BASE_LEVEL_FIELD_NAMe = "baseLevel";

    @EntityAttribute(BASE_LEVEL_FIELD_NAMe)
    private int level;

    private final Sprite sprite;

    private float speed;
    private float direction;

    private Player owner;

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    private final B entityType;

    public Entity(B entityType, Player owner, float x, float y) {
        initializeConstructorDependencies();

        this.entityType = entityType;
        this.owner = owner;
        this.sprite = new Sprite(entityType.getTexture());

        setSize(entityType.getTexture().getWidth() / 16, entityType.getTexture().getHeight() / 16);
        setPosition(x, y);
        setOrigin(Align.center);

        // Higher Z Index Correlates to Significant (Lower) Priority. Z Index determines drawing order of actors
        setZIndex(EntityType.NUMBER_OF_ENTITY_TYPES - getEntityType().getEntityTypeIndex());

        updateSprite();

        EntityAttributeInitializer.initializeAttributes(getReference());
    }

    /**
     * Returns the instance of the subclass rather than the superclass. Implementations must return {@code this} in
     * the context of that subclass.
     *
     * @return A reference to the subclass rather than the superclass, i.e {@code this} in the context of the subclass,
     * not superclass.
     */
    protected abstract A getReference();

    public abstract boolean shouldRemove();

    public Player getOwner() {
        return owner;
    }

    private Sprite getSprite() {
        return sprite;
    }

    private void updateSprite() {
        getSprite().setBounds(getX(), getY(), getWidth(), getHeight());
        getSprite().setOriginCenter();
    }

    private void wrapPosition() {
        final float lowerBoundedX = Math.max(0, getX());
        final float doubleBoundedX = Math.min(getX(), MatchScreen.WHOLE_WORLD_TILE_WIDTH);

        final float lowerBoundedY = Math.max(0, getY());
        final float doubleBoundedY = Math.min(getY(), MatchScreen.WHOLE_WORLD_TILE_HEIGHT);

        setX(doubleBoundedX);
        setY(doubleBoundedY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateSprite();
        wrapPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getSprite().draw(batch);
    }

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any NON-VISUAL updates. This should be followed up by {@link #present(Camera, Stage, ShapeRenderer)}
     */
    public abstract void check(float delta, Player player, Match match);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any VISUAL updates. This should follow {@link #check(float, Player, Match)}
     */
    public abstract void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer);

    public B getEntityType() {
        return entityType;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Initializes fields that a super class constructor is dependent upon, prior to calling said super constructor.
     */
    public void initializeConstructorDependencies() {
        // No default implementation
    }

    public void setRotation(float angle) {
        getSprite().setRotation(angle);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }


}
