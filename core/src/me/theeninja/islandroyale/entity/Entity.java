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

    @EntityAttribute
    private int level;

    private final Sprite sprite;

    /**
     * X is distance to move per secod
     * Y is angle relative to positive x axis that signifies direction, in radians
     */
    private final Vector2 velocityPerSecond = new Vector2(0, 0);

    private final Player owner;

    private final B entityType;

    public Entity(B entityType, Player owner, float x, float y) {
        this.entityType = entityType;
        this.owner = owner;
        this.sprite = new Sprite(entityType.getTexture());

        // `this` would refer to an instance of Entity<A, B> rather than A, getReference() returns the instance of the
        // subclass (type A) rather than an instance of the superclass (type Entity<A, B>)
        getEntityType().setUpEntity(getReference());

        getSprite().setSize(entityType.getTexture().getWidth() / 16, entityType.getTexture().getHeight() / 16);
        getSprite().setPosition(x, y);
        getSprite().setOriginCenter();
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

    public Vector2 getVelocityPerSecond() {
        return velocityPerSecond;
    }

    public void setSpeed(float speed) {
        getVelocityPerSecond().x = speed;
    }

    public void setDirection(float angle) {
        getVelocityPerSecond().y = angle;
    }

    public float getSpeed() {
        return getVelocityPerSecond().x;
    }

    public float getDirection() {
        return getVelocityPerSecond().y;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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
    public abstract void present(Camera projector, Stage stage, ShapeRenderer shapeRenderer);

    public B getEntityType() {
        return entityType;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
