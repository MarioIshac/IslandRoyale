package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectileType;
import me.theeninja.islandroyale.gui.screens.Match;

public final class DefenseBuilding extends Building<DefenseBuilding, DefenseBuildingType> implements Attacker<DefenseBulletProjectile, DefenseBulletProjectileType, DefenseBuilding, DefenseBuildingType> {
    @EntityAttribute(BASE_RANGE_FIELD_NAME)
    private float range;

    @EntityAttribute(BASE_FIRE_RATE_FIELD_NAME)
    private float fireRate;

    @EntityAttribute(BASE_DAMAGE_FIELD_NAME)
    private float damage;

    private float timeUntilAttack;
    private InteractableEntity<?, ?> targetEntity;

    private final Sprite rangeSprite;

    public DefenseBuilding(DefenseBuildingType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);

        setRange(getEntityType().getBaseRange());
        setDamage(getEntityType().getBaseDamage());
        setFireRate(getEntityType().getBaseFireRate());

        FileHandle rangeFileHandle = Gdx.files.internal("Range.png");
        Texture rangeTexture = new Texture(rangeFileHandle);
        this.rangeSprite = new Sprite(rangeTexture);

        getRangeSprite().setSize(1, 1);
    }

    @Override
    protected DefenseBuilding getReference() {
        return this;
    }

    @Override
    public void check(float delta, Player player, Match match) {
        super.check(delta, player, match);

        System.out.println("Checking");

        // Does 2 things:
        // 1) Ensures that there is always the requested field within map (obvious)
        // 2) Ensures that upon entity construction, a shot is not fired IMMEDIATELY. Rather, it is fired
        // after a delay equivalent to the required elapsed seconds
        setTimeUntilAttack(getTimeUntilAttack() - delta);

        if (getTimeUntilAttack() > 0) {
            System.out.println("Waiting to attack with " + getTimeUntilAttack() + " remaining");
            return;
        }

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(this)) {
            InteractableEntity<?, ?> newTargetEntity = getNewTargetEntity(this, match.getMatchMap());
            setTargetEntity(newTargetEntity);

            System.out.println("Target Entity " + getTargetEntity());

            // If the NEW target entity is also null, that indicates that no entities are within range
            if (newTargetEntity == null) {
                System.out.println("No entities within range");
                return;
            }
        }

        DefenseBulletProjectile projectile = newProjectile(this, match);
        match.getMatchMap().addEntitySafely(projectile);

        System.out.println("Launched Attack");

        setTimeUntilAttack(1 / getFireRate());
    }

    /**
     * {@inheritDoc}
     *
     * This implementation shows a circle around the entity used to signify the range
     * (in tiles) of said entity.
     */
    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(projector, hudStage, shapeRenderer);

        getRangeSprite().setPosition(getX(), getY());
        System.out.println("(" + getX() + " " +getY() + ")");
        getRangeSprite().setOriginCenter();

        // Scaling wll scale the width by the argument, so we must supply diameter
        getRangeSprite().setScale(getRange() * 2);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        getRangeSprite().draw(getStage().getBatch());

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public int getStaticProjectileID() {
        return getEntityType().getStaticProjectileID();
    }

    @Override
    public DefenseBulletProjectile newGenericProjectile(DefenseBulletProjectileType projectileType, Player player, float x, float y, DefenseBuilding attackerEntity) {
        return new DefenseBulletProjectile(projectileType, player, x, y, attackerEntity);
    }

    @Override
    public float getRange() {
        return this.range;
    }

    @Override
    public void setRange(float range) {
        this.range = range;
    }

    @Override
    public float getTimeUntilAttack() {
        return this.timeUntilAttack;
    }

    @Override
    public void setTimeUntilAttack(float timeUntilAttack) {
        this.timeUntilAttack = timeUntilAttack;
    }

    @Override
    public InteractableEntity<?, ?> getTargetEntity() {
        return this.targetEntity;
    }

    @Override
    public void setTargetEntity(InteractableEntity<?, ?> targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    public float getFireRate() {
        return this.fireRate;
    }

    @Override
    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Sprite getRangeSprite() {
        return rangeSprite;
    }
}
