package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TransportAcceptorListener extends InputListener {
    private final Entity<TransportEntityType> entity;
    private final List<Entity<TransportEntityType>> transporters = new ArrayList<>();

    TransportAcceptorListener(Entity<TransportEntityType> entity) {
        this.entity = entity;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Transport added");

        Entity<PersonEntityType> personRequesting = EntityType.getProperty(getEntity(), TransportEntityType.TRANSPORT_REQUEST_LABEL);

        // Indicates that there is no person requesting to be transported
        if (personRequesting == null)
            return false; // Return false, indicating that other lick events such as show descriptor can be processed

        EntityType.setProperty(personRequesting, PersonEntityType.IS_CARRIED_LABEL, true);

        List<Entity<PersonEntityType>> personsCarried = EntityType.getProperty(
                getEntity(),
                TransportEntityType.CARRIED_ENTITIES_LABEL
        );

        personsCarried.add(personRequesting);
        int numberOfEntitiesCarried = EntityType.getProperty(getEntity(), TransportEntityType.NUMBER_OF_ENTITIES_CARRIED_LABEL);
        EntityType.setProperty(getEntity(), TransportEntityType.NUMBER_OF_ENTITIES_CARRIED_LABEL, numberOfEntitiesCarried);
        System.out.println("Person has been added");

        return true;
    }

    public Entity<TransportEntityType> getEntity() {
        return entity;
    }
}