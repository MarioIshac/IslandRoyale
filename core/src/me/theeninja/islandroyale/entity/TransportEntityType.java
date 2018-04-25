package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

public class TransportEntityType extends MovingEntityType<TransportEntityType> {
    public static final String PERSON_DIRECTORY = "person/";
    private static final String CARRIED_ENTITIES_LABEL = "carriedEntities";

    @Override
    public void initialize(Entity<MovingEntityType<TransportEntityType>> entity) {
        super.initialize(entity);

        entity.getProperties().put(CARRIED_ENTITIES_LABEL, new Array<Entity<MovingEntityType<PersonEntityType>>>());
    }

    @Override
    public void check(Entity<MovingEntityType<TransportEntityType>> entity, float delta, Player player, MatchMap matchMap) {
        move(entity, delta);

        Array<Entity<MovingEntityType<PersonEntityType>>> carried = getProperty(entity, CARRIED_ENTITIES_LABEL);

        for (Entity<MovingEntityType<PersonEntityType>> person : carried)
            setProperty(person, PersonEntityType.IS_CARRIED_LABEL, true);
    }

    @Override
    public void present(Entity<MovingEntityType<TransportEntityType>> entity, Batch batch, int centerPixelX, int centerPixelY) {

    }
}
