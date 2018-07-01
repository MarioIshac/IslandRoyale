package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;

public class Person extends ControllableEntity<Person, PersonType> implements Attacker<PersonBulletProjectile, PersonBulletProjectileType, Person, PersonType> {
    @EntityAttribute
    private float range;

    @EntityAttribute
    private float fireRate;

    @EntityAttribute
    private float damage;

    private float timeUntilAttack;
    private InteractableEntity<?, ?> targetEntity;

    private final TransportInitiatorListener transportInitiatorListener = new TransportInitiatorListener(this);

    private Transporter carrier;

    public Person(PersonType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected Person getReference() {
        return this;
    }

    @Override
    public void check(float timeChange, Player player, MatchMap matchMap) {
        super.check(timeChange, player, matchMap);

        // Update all transporters within the load to transport listener.
        getTransportInitiatorListener().getTransporters().clear();

        for (Entity<?, ?> matchMapEntity : matchMap.getEntities()) {
            if (!(matchMapEntity.getEntityType() instanceof TransporterType))
                continue;

            Transporter transporter = (Transporter) matchMapEntity;

            getTransportInitiatorListener().getTransporters().add(transporter);
        }

        // If this person is being carried by transporation, let the transporter
        // take care of controllable this entity
        if (getCarrier() != null)
            setSpeed(0);
        else
            updateMoveAttributes();

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(this)) {
            InteractableEntity<?, ?> targetEntity = getNewTargetEntity(this, matchMap);
            setTargetEntity(targetEntity);
        }

        attackIfRequired(timeChange);
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

    public TransportInitiatorListener getTransportInitiatorListener() {
        return transportInitiatorListener;
    }

    public Transporter getCarrier() {
        return carrier;
    }

    public void setCarrier(Transporter carrier) {
        this.carrier = carrier;
    }
}
