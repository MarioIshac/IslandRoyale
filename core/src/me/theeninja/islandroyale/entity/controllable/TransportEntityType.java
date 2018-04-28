package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;

public class TransportEntityType extends ControllableEntityType<TransportEntityType> {
    public static final String PERSON_DIRECTORY = "person/";
    private static final String CARRIED_ENTITIES_LABEL = "carriedEntities";

    @Override
    public void initialize(Entity<TransportEntityType> entity) {
        super.initialize(entity);

        entity.getProperties().put(CARRIED_ENTITIES_LABEL, new Array<Entity<PersonEntityType>>());
    }

    @Override
    public void check(Entity<TransportEntityType> entity, float delta, Player player, MatchMap matchMap) {
        updateMoveAttributes(entity, delta);

        Array<Entity<PersonEntityType>> carried = getProperty(entity, CARRIED_ENTITIES_LABEL);

        for (Entity<PersonEntityType> person : carried)
            setProperty(person, PersonEntityType.IS_CARRIED_LABEL, true);
    }

    @Override
    public void present(Entity<TransportEntityType> entity, Batch batch, float centerPixelX, float centerPixelY) {

    }
}
