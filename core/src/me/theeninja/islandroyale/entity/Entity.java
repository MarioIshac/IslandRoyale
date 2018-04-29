package me.theeninja.islandroyale.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

import java.util.HashMap;
import java.util.Map;

public class Entity<T extends EntityType<T>> {
    private final Vector2 pos;
    private final Vector2 velocityPerSecond;
    private float rotationPerSecond;

    private final Player owner;

    private final T entityType;

    private final Map<String, Object> properties = new HashMap<>();

    public Entity(T entityType, Player owner, Vector2 position) {
        this.entityType = entityType;
        this.owner = owner;
        this.pos = position;
        this.velocityPerSecond = new Vector2(0, 0);
        this.rotationPerSecond = 0;

        getType().initialize(this);
    }

    public T getType() {
        return entityType;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void check(float delta, Player player, MatchMap matchMap) {
        getType().check(this, delta, player, matchMap);
    }

    public void present(Batch batch, float tileX, float tileY) {
        getType().present(this, batch, tileX, tileY);
    }

    public boolean shouldRemove() {
        return getType().shouldRemove(this);
    }

    public Player getOwner() {
        return owner;
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getRotationPerSecond() {
        return rotationPerSecond;
    }

    public void setRotationPerSecond(float rotationPerSecond) {
        this.rotationPerSecond = rotationPerSecond;
    }

    public Vector2 getVelocityPerSecond() {
        return velocityPerSecond;
    }
}
