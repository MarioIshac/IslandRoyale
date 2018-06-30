package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TransportEntityType extends ControllableEntityType<TransportEntityType> {
    public static final String CARRIED_ENTITIES_LABEL = "carriedEntities";
    public static final String TRANSPORT_REQUEST_LABEL = "transportRequest";
    public static final String NUMBER_OF_ENTITIES_CARRIED_LABEL = "numberOfEntities";
    private static final String TRANSPORT_ACCEPTOR_LISTENER_LABEL = "transportAcceptorListener";

    /**
     * Represents the number of pixels on the texture's edge that should not be allocated for
     * positioning people on the transport. This to make it so people look "comfortable" placed
     * on a boat.
     */
    private int pixelMargin;

    @Override
    public int getDrawingPriority() {
        return 1;
    }

    @Override
    public void setUp(Entity<TransportEntityType> entity) {
        super.setUp(entity);

        TransportAcceptorListener transportAcceptorListener = new TransportAcceptorListener(entity);

        entity.addListener(transportAcceptorListener);

        setProperty(entity, TRANSPORT_ACCEPTOR_LISTENER_LABEL, transportAcceptorListener);

        entity.getProperties().put(CARRIED_ENTITIES_LABEL, new ArrayList<Entity<PersonEntityType>>());
        setProperty(entity, NUMBER_OF_ENTITIES_CARRIED_LABEL, 0);
    }

    @Override
    public void check(Entity<TransportEntityType> transporter, float delta, Player player, MatchMap matchMap) {
        super.check(transporter, delta, player, matchMap);

        // Update all transporters within the load to transport listener.
        TransportAcceptorListener transportAcceptorListener = getProperty(transporter, TRANSPORT_ACCEPTOR_LISTENER_LABEL);
        transportAcceptorListener.getTransporters().clear();

        for (Entity<? extends EntityType<?>> matchMapEntity : matchMap.getEntities()) {
            if (!(matchMapEntity.getType() instanceof TransportEntityType))
                continue;

            Entity<TransportEntityType> transportEntityTypeEntity = (Entity<TransportEntityType>) matchMapEntity;

            transportAcceptorListener.getTransporters().add(transportEntityTypeEntity);
        }


        //

        updateMoveAttributes(transporter);

        List<Entity<PersonEntityType>> carried = getProperty(transporter, CARRIED_ENTITIES_LABEL);

        int commulativeTotal = 0;

        float positionOffset = getPixelMargin() / 16f;

        for (Entity<PersonEntityType> person : carried) {

            float personXPos = positionOffset + transporter.getSprite().getX() + commulativeTotal;
            float personYPos = positionOffset + transporter.getSprite().getY();

            person.getSprite().setPosition(personXPos, personYPos);
            commulativeTotal += person.getSprite().getWidth();
        }
    }

    @Override
    public void present(Entity<TransportEntityType> entity, Camera mapCamera, Stage hudStage) {
        super.present(entity, mapCamera, hudStage);
    }

    public int getPixelMargin() {
        return pixelMargin;
    }
}