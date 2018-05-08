package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TransportEntityType extends ControllableEntityType<TransportEntityType> {
    public static final String CARRIED_ENTITIES_LABEL = "carriedEntities";
    public static final String TRANSPORT_REQUEST_LABEL = "transportRequest";
    public static final String NUMBER_OF_ENTITIES_CARRIED_LABEL = "numberOfEntities";

    @Override
    public void setUp(Entity<TransportEntityType> entity) {
        super.setUp(entity);

        TransportAcceptorListener transportAcceptorListener = new TransportAcceptorListener(entity);

        entity.addListener(transportAcceptorListener);

        entity.getProperties().put(CARRIED_ENTITIES_LABEL, new ArrayList<Entity<PersonEntityType>>());
        setProperty(entity, NUMBER_OF_ENTITIES_CARRIED_LABEL, 0);
    }

    @Override
    public void check(Entity<TransportEntityType> entity, float delta, Player player, MatchMap matchMap) {
        super.check(entity, delta, player, matchMap);

        setDefaultSpeed(entity);

        List<Entity<PersonEntityType>> carried = getProperty(entity, CARRIED_ENTITIES_LABEL);

        int commulativeTotal = 0;

        for (Entity<PersonEntityType> person : carried) {
            setProperty(person, PersonEntityType.IS_CARRIED_LABEL, true);
            person.getSprite().setPosition(entity.getSprite().getX() + commulativeTotal, entity.getSprite().getY());
            commulativeTotal += person.getSprite().getWidth();
        }
    }

    @Override
    public void present(Entity<TransportEntityType> entity, Camera projector, Stage stage) {
        super.present(entity, projector, stage);
    }
}
