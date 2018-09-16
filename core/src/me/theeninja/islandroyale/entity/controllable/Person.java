package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;
import me.theeninja.islandroyale.gui.screens.Match;

public class Person extends ControllableEntity<Person, PersonType> implements Attacker<PersonBulletProjectile, PersonBulletProjectileType, Person, PersonType> {
    @EntityAttribute
    private float range;

    @EntityAttribute
    private float fireRate;

    @EntityAttribute
    private float damage;

    private float timeUntilAttack;
    private InteractableEntity<?, ?> targetEntity;
    private boolean aggressive;

    private TransportInitiatorListener transportListener;

    @Override
    public void initializeConstructorDependencies() {
        super.initializeConstructorDependencies();

        this.transportListener = new TransportInitiatorListener(this);;
    }

    private Transporter carrier;

    public Person(PersonType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected Person getReference() {
        return this;
    }

    public static final float ANGLE_CHANGE_FACTOR = 1;

    @Override
    public void check(float timeChange, Player player, Match match) {
        super.check(timeChange, player, match);

        getTransportListener().refreshTransporters(match.getMatchMap().getAllPriorityEntities());

        // If this person is being carried by transporation, let the transporter
        // take care of controllable this entity
        if (getCarrier() != null)
            setSpeed(0);
        else
            updateMoveAttributes();

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(this)) {
            InteractableEntity<?, ?> targetEntity = getNewTargetEntity(this, match.getMatchMap());
            setTargetEntity(targetEntity);
        }

        if (getTargetEntity() != null) {
            attackIfRequired(timeChange);

            if (isAggressive()) {
                float xDistanceFromTarget = getTargetEntity().getX() - getX();
                float yDistanceFromTarget = getTargetEntity().getY() - getY();

                float angleToTarget = (float) Math.atan2(yDistanceFromTarget, xDistanceFromTarget);

                float angleAdjustmentRequired = angleToTarget - getDirection();

                float angleAdjustment = Math.signum(angleAdjustmentRequired) * ANGLE_CHANGE_FACTOR;

                setDirection(getDirection() + angleAdjustment);
            }
        }
    }

    @Override
    protected boolean calculateUpgradable() {
        return false;
    }

    private void attackIfRequired(float delta) {
        setTimeUntilAttack(getTimeUntilAttack() - delta);

        if (getTimeUntilAttack() > 0)
            return;

        setTimeUntilAttack(1 / getEntityType().getBaseFireRate());

        float currentTargetHealth = getTargetEntity().getHealth();
        currentTargetHealth -= getDamage();
        getTargetEntity().setHealth(currentTargetHealth);

    }

    @Override
    public int getStaticProjectileID() {
        return 0;
    }

    @Override
    public PersonBulletProjectile newGenericProjectile(PersonBulletProjectileType projectileType, Player player, float x, float y, Person attackerEntity) {
        return new PersonBulletProjectile(projectileType, player, x, y, attackerEntity);
    }

    @Override
    public float getRange() {
        return range;
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
        return this.damage;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public TransportInitiatorListener getTransportListener() {
        return transportListener;
    }

    public Transporter getCarrier() {
        return carrier;
    }

    public void setCarrier(Transporter carrier) {
        this.carrier = carrier;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }
}
