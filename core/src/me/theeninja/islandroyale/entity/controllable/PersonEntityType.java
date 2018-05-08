package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Offensive;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public class PersonEntityType extends ControllableEntityType<PersonEntityType> implements Offensive<PersonEntityType> {
    public static final String PERSON_DIRECTORY = "person/";

    private float baseDamage;
    private float baseFireRate;

    public static final String IS_CARRIED_LABEL = "isCarried";

    private final static String TIME_LEFT_LABEL = "timeLeft";

    private final static String LOAD_TO_TRANSPORT_LISTENER_LABEL = "loadToTransportLabel";

    @Override
    public void setUp(Entity<PersonEntityType> entity) {
        super.setUp(entity);

        setProperty(entity, IS_CARRIED_LABEL, false);
        entity.getProperties().put(TIME_LEFT_LABEL, 1 / getBaseFireRate());
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
        //

        boolean isCarried = getProperty(entity, IS_CARRIED_LABEL);

        // If this person is being carried by transporation, let the transporter
        // take care of moving this entity
        if (isCarried)
            entity.setSpeed(0);
        else {
            Vector3 targetCoords = getProperty(entity, TARGET_COORDS_LABEL);

            // If no target, no need to move
            if (targetCoords == null)
                entity.setSpeed(0);
            else {
                float currentX = entity.getSprite().getX();
                float currentY = entity.getSprite().getY();

                setDefaultSpeed(entity);

                float angle = (float) Math.atan(currentY / currentX);

                if (currentX < 0)
                    angle += Math.PI / 2;

                entity.setDirection(angle);
            }
        }

        Entity<? extends InteractableEntityType<?>> currentTargetEntity = getProperty(entity, ATTACKING_TARGET_LABEL);

        // If the current target entity has expired, i.e a new target entity is required
        if (isNewTargetEntityRequired(currentTargetEntity))
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
    public void present(Entity<PersonEntityType> entity, Camera projector, Stage stage) {
        super.present(entity, projector, stage);
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
}
