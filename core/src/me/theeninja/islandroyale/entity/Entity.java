package me.theeninja.islandroyale.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

import java.util.HashMap;
import java.util.Map;

public class Entity<T extends EntityType<T>> {
    private final Vector2 pos;
    private final Vector2 velocity;
    private float rotationPerSecond;

    private final Player owner;

    public static boolean isEntityDead(Map.Entry<Entity<? extends EntityType<?>>, Vector2> entry) {
        return entry.getKey().getHealth() <= 0;
    }

    private final T entityType;

    private int level = 1;
    private float health;

    private final Map<String, Object> properties = new HashMap<>();

    public Entity(T entityType, Player owner, Vector2 position) {
        this.entityType = entityType;
        this.health = entityType.getBaseHealth();
        this.owner = owner;
        this.pos = position;
        this.velocity = new Vector2(0, 0);
        this.rotationPerSecond = 0;

        getType().initialize(this);
    }

    public int getLevel() {
        return level;
    }

    public float getHealth() {
        return health;
    }

    public void dealDamage(float damage) {
        health -= damage;
    }

    public T getType() {
        return entityType;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void upgrade() {
        this.level += 1;
    }

    public void check(float delta, Player player, MatchMap matchMap) {
        getType().check(this, delta, player, matchMap);
    }

    public void present(Batch batch, float tileX, float tileY) {
        getType().present(this, batch, tileX, tileY);
    }

    public Player getOwner() {
        return owner;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getRotationPerSecond() {
        return rotationPerSecond;
    }

    public void setRotationPerSecond(float rotationPerSecond) {
        this.rotationPerSecond = rotationPerSecond;
    }
}
