package me.theeninja.islandroyale.entity;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class Entity<T extends EntityType<T>> {
    public static boolean isEntityDead(Map.Entry<Entity<? extends EntityType>, Vector2> entry) {
        return entry.getKey().getHealth() <= 0;
    }

    private final T entityType;

    private int level = 1;
    private float health;

    private final Map<String, Object> properties = new HashMap<>();

    public Entity(T entityType) {
        this.entityType = entityType;

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
}
