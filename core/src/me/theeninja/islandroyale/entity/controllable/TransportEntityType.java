package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TransportEntityType extends ControllableEntityType<TransportEntityType> {
    public static final String PERSON_DIRECTORY = "person/";
    private static final String CARRIED_ENTITIES_LABEL = "carriedEntities";

    @Override
    public void setUp(Entity<TransportEntityType> entity) {
        super.setUp(entity);

        entity.getProperties().put(CARRIED_ENTITIES_LABEL, new ArrayList<Entity<PersonEntityType>>());
    }

    @Override
    public void check(Entity<TransportEntityType> entity, float delta, Player player, MatchMap matchMap) {
        setDefaultSpeed(entity);

        List<Entity<PersonEntityType>> carried = getProperty(entity, CARRIED_ENTITIES_LABEL);

        for (Entity<PersonEntityType> person : carried)
            setProperty(person, PersonEntityType.IS_CARRIED_LABEL, true);
    }

    @Override
    public void present(Entity<TransportEntityType> entity, Stage stage) {

    }
}
