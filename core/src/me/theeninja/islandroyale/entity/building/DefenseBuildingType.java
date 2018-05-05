package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.*;

public class DefenseBuildingType extends BuildingEntityType<DefenseBuildingType> implements Offensive<DefenseBuildingType> {

    private Sprite rangeSprite;

    private float baseDamage;

    /**
     * Represents the shots per second to be shot from the defense building. Note that if this is greater
     * than the frames per second of the application, the fire rate will functionally be lowered
     * to the frames per second itself due to how this is handled.
     */
    private float baseFireRate;

    /**
     * Represents the ID of the static projectile that this defense building fires (such as a cannon ball,
     * arrow, bullet, etc).
     */
    private int staticProjectileID;

    public float getBaseFireRate() {
        return baseFireRate;
    }

    private static final String SECONDS_ELAPSED_SINCE_LAST_SHOT = "secondsElapsed";

    @Override
    public void configureEditor(Entity<DefenseBuildingType> entity, VerticalGroup verticalGroup) {

    }

    @Override
    public void setUp(Entity<DefenseBuildingType> entity) {
        super.setUp(entity);
        setProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT, 1 / getBaseFireRate());

        FileHandle rangeFileHandle = Gdx.files.internal("Range.png");
        Texture rangeTexture = new Texture(rangeFileHandle);
        this.rangeSprite = new Sprite(rangeTexture);

        getRangeSprite().setSize(1, 1);
    }

    boolean dealingDamage;

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

        Entity<? extends InteractableEntityType<?>> currentTargetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(currentTargetEntity)) {
            currentTargetEntity = getNewTargetEntity(entity, matchMap, getBaseRange());

            setProperty(entity, ATTACKING_TARGET_LABEL, currentTargetEntity);

            // If the NEW target entity is also null, that indicates that no entities are within range
            if (currentTargetEntity == null)
                return;
        }

        StaticProjectileEntityType projectileType = EntityType.getEntityType(getStaticProjectileID());

        Vector2 pos = new Vector2(
                entity.getSprite().getX(),
                entity.getSprite().getY()
        );

        Entity<StaticProjectileEntityType> projectile = new Entity<>(projectileType, entity.getOwner(), pos);

        projectile.setSpeed(10);

        projectileType.externalInitialize(projectile, entity, currentTargetEntity);

        matchMap.getEntities().add(projectile);

        setProperty(entity, SECONDS_ELAPSED_SINCE_LAST_SHOT, 1 / getBaseFireRate());
    }

    /**
     * {@inheritDoc}
     *
     * This implementation shows a circle around the entity used to signify the range
     * (in tiles) of said entity.
     */
    @Override
    public void present(Entity<DefenseBuildingType> entity, Stage stage) {
        super.present(entity, stage);

        getRangeSprite().setPosition(entity.getSprite().getX(), entity.getSprite().getY());
        getRangeSprite().setOriginCenter();
        getRangeSprite().setScale(getBaseRange());

        int currentLevel = getProperty(entity, LEVEL_LABEL);
        float tileRange = getRangeMultiplier(currentLevel);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        getRangeSprite().draw(stage.getBatch());

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private float getRangeMultiplier(int level) {
        float result = getBaseRange();

        while (level-- > 1)
            result *= 1.1;

        return result;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    private float attack(float health) {
        return damageHealth(health, getBaseDamage());
    }

    public int getStaticProjectileID() {
        return staticProjectileID;
    }

    public Sprite getRangeSprite() {
        return rangeSprite;
    }
}
