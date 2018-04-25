package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

import java.util.List;

public class DefenseBuildingType extends BuildingEntityType<DefenseBuildingType> implements Offensive<DefenseBuildingType> {
    private float baseDamage;

    /**
     * Represents the shots per second to be shot from the defense building. Note that if this is greater
     * than the frames per second of the application, the fire rate will functionally be lowered
     * to the frames per second itself due to how this is handled.
     */
    private float baseFireRate;

    /**
     * Represents the range of the defense building in terms of tiles on the map. This serves as the
     * radius of the circle that is the zone of which the defence building can shoot in.
     */
    private float baseRange;

    public float getBaseRange() {
        return baseRange;
    }

    public float getBaseFireRate() {
        return baseFireRate;
    }

    private static final String SECONDS_ELAPSED_SINCE_LAST_SHOT = "secondsElapsed";

    @Override
    public void initialize(Entity<DefenseBuildingType> entity) {
        setProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT, 1 / getBaseFireRate());
    }

    @Override
    public void check(Entity<DefenseBuildingType> entity, float delta, Player player, MatchMap matchMap) {
        // Does 2 things:
        // 1) Ensures that there is always the requested field within map (obvious)
        // 2) Ensures that upon entity construction, a shot is not fired IMMEDIATELY. Rather, it is fired
        // after a delay equivalent to the required elapsed seconds
        float secondsElapsedSinceLastShot = getProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT);
        setProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT, secondsElapsedSinceLastShot - delta);

        if (secondsElapsedSinceLastShot > 0)
            return;

        Entity<? extends EntityType> currentTargetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);

        // If the current target entity has expired, i.e a new target entity is required
        if (currentTargetEntity == null || currentTargetEntity.getHealth() <= 0)
            setProperty(entity, ATTACKING_TARGET_LABEL, getNewTargetEntity(entity, matchMap));

        currentTargetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);
        currentTargetEntity.dealDamage(getBaseDamage());

        setProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT, 1 / getBaseFireRate());
    }

    /**
     * {@inheritDoc}
     *
     * This implementation shows a circle around the entity used to signify the range
     * (in tiles) of said entity.
     */
    @Override
    public void present(Entity<DefenseBuildingType> entity, Batch batch, int centerPixelX, int centerPixelY) {
        float tileRange = getRangeMultiplier(entity.getLevel());

        int tilePixels = Math.round(tileRange * 16);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(150/255f, 150/255f, 150/255f, 0.5f);
        shapeRenderer.circle(centerPixelX, centerPixelY, tilePixels);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private float getRangeMultiplier(int level) {
        float result = baseRange;

        while (level-- > 1)
            result *= 1.1;

        return result;
    }

    public float getBaseDamage() {
        return baseDamage;
    }
}
