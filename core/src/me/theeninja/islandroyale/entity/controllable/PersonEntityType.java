package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Attacker;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public class PersonEntityType extends ControllableEntityType<PersonEntityType> implements Attacker<PersonEntityType> {
    public static final String PERSON_DIRECTORY = "person/";

    private float baseDamage;
    private float baseFireRate;

    public static final String CARRIER_LABEL = "carrier";

    private final static String TIME_LEFT_LABEL = "timeLeft";

    private final static String LOAD_TO_TRANSPORT_LISTENER_LABEL = "loadToTransportLabel";

    @Override
    public int getDrawingPriority() {
        return 2;
    }

    @Override
    public void setUp(Entity<PersonEntityType> entity) {
        super.setUp(entity);

        setProperty(entity, TIME_LEFT_LABEL, 1 / getBaseFireRate());
    }

    @Override
    public void configureEditor(Entity<PersonEntityType> entity, Table table) {
        super.configureEditor(entity, table);

        TextButton transportButton = new TextButton("Transport", MatchScreen.FLAT_EARTH_SKIN);
        TransportInitiatorListener transportInitiatorListener = new TransportInitiatorListener(entity);

        table.add(transportButton).row();

        transportButton.addListener(transportInitiatorListener);

        setProperty(entity, LOAD_TO_TRANSPORT_LISTENER_LABEL, transportInitiatorListener);
    }

    @Override
    public void check(Entity<PersonEntityType> entity, float delta, Player player, MatchMap matchMap) {
        super.check(entity, delta, player, matchMap);

        // Update all transporters within the load to transport listener.
        TransportInitiatorListener transportInitiatorListener = getProperty(entity, LOAD_TO_TRANSPORT_LISTENER_LABEL);
        transportInitiatorListener.getTransporters().clear();

        for (Entity<? extends EntityType<?>> matchMapEntity : matchMap.getEntities()) {
            if (!(matchMapEntity.getType() instanceof TransportEntityType))
                continue;

            Entity<TransportEntityType> transportEntityTypeEntity = (Entity<TransportEntityType>) matchMapEntity;

            transportInitiatorListener.getTransporters().add(transportEntityTypeEntity);
        }

        Entity<TransportEntityType> carrier = getProperty(entity, CARRIER_LABEL);

        // If this person is being carried by transporation, let the transporter
        // take care of moving this entity
        if (carrier != null)
            entity.setSpeed(0);
        else
            updateMoveAttributes(entity);

        Entity<? extends InteractableEntityType<?>> currentTargetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(entity, currentTargetEntity))
            setProperty(entity,ATTACKING_TARGET_LABEL, getNewTargetEntity(entity, matchMap, getBaseDamage()));

        performDamageCheck(entity, delta);
    }

    private void performDamageCheck(Entity<PersonEntityType> entity, float delta) {
        float timeLeft = getProperty(entity, TIME_LEFT_LABEL);
        timeLeft -= delta;

        if (timeLeft <= 0) {
            setProperty(entity, TIME_LEFT_LABEL, 1 / getBaseFireRate());
            Entity<? extends InteractableEntityType<?>> targetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);

            changeProperty(targetEntity, HEALTH_LABEL, this::attack);
        }
        else
            setProperty(entity, TIME_LEFT_LABEL, timeLeft);
    }

    @Override
    public void present(Entity<PersonEntityType> entity, Camera mapCamera, Stage hudStage) {
        super.present(entity, mapCamera, hudStage);
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getBaseFireRate() {
        return baseFireRate;
    }

    private float attack(float health) {
        return damageHealth(health, getBaseDamage());
    }

    @Override
    public int getStaticProjectileID() {
        return -1;
    }
}
