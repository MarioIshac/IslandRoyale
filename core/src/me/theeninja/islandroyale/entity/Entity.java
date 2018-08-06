package me.theeninja.islandroyale.entity;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

public abstract class Entity<A extends Entity<A, B>, B extends EntityType<A, B>> extends Actor {
    public static float rangeBetweenSquared(Entity<?, ?> entityOne, Entity<?, ?> entityTwo) {
        float xDiff = entityOne.getSprite().getX() - entityTwo.getSprite().getY();
        float yDiff = entityOne.getSprite().getY() - entityTwo.getSprite().getY();

        return xDiff * xDiff + yDiff * yDiff;
    }

    public static float rangeBetween(Entity<?, ?> entityOne, Entity<?, ?> entityTwo) {
        return (float) Math.sqrt(rangeBetweenSquared(entityOne, entityTwo));
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

    @EntityAttribute
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

        getSprite().setSize(entityType.getTexture().getWidth() / 16, entityType.getTexture().getHeight() / 16);
        getSprite().setPosition(x, y);
        getSprite().setOriginCenter();

        // Higher Z Index Correlates to Significant (Lower) Priority. Z Index determines drawing order of actors
        setZIndex(EntityType.NUMBER_OF_PRIORITIES - getEntityType().getDrawingPriority());
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

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setBounds(getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        getSprite().draw(batch);
    }

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any NON-VISUAL updates. This should be followed up by {@link #present(Camera, Stage, ShapeRenderer)}
     */
    public abstract void check(float delta, Player player, MatchMap matchMap);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any VISUAL updates. This should follow {@link #check(float, Player, MatchMap)}
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
