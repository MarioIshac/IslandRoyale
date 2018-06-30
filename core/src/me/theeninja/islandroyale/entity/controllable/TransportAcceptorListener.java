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


        Entity<PersonEntityType> personRequesting = EntityType.getProperty(getEntity(), TransportEntityType.TRANSPORT_REQUEST_LABEL);

        // Indicates that there is no person requesting to be transported
        if (personRequesting == null)
            return false; // Return false, indicating that other lick events such as show descriptor can be processed

        List<Entity<PersonEntityType>> personsCarried = EntityType.getProperty(
                getEntity(),
                TransportEntityType.CARRIED_ENTITIES_LABEL
        );

        Entity<TransportEntityType> currentTransporter = EntityType.getProperty(personRequesting, PersonEntityType.CARRIER_LABEL);

        // Indicates that the user's intent is to transfer the person from one transproter to ANOTHER transproter
        // which we handle by first removing person from original handler
        if (currentTransporter != null) {
            List<Entity<PersonEntityType>> transportedPersons = EntityType.getProperty(currentTransporter, TransportEntityType.CARRIED_ENTITIES_LABEL);

            transportedPersons.remove(personRequesting);
        }

        // Now, old transporter (if any) is irrelevant, and simply add person to new transport
        EntityType.setProperty(personRequesting, PersonEntityType.CARRIER_LABEL, getEntity());
        personsCarried.add(personRequesting);
        int numberOfEntitiesCarried = EntityType.getProperty(getEntity(), TransportEntityType.NUMBER_OF_ENTITIES_CARRIED_LABEL);
        EntityType.setProperty(getEntity(), TransportEntityType.NUMBER_OF_ENTITIES_CARRIED_LABEL, numberOfEntitiesCarried);

        // Tell other transporters that person has been added, therefore other transporters
        // need not listen for person boarding it anymore.
        for (Entity<TransportEntityType> transporter : getTransporters())
            EntityType.setProperty(transporter, TransportEntityType.TRANSPORT_REQUEST_LABEL, null);

        return true;
    }

    public Entity<TransportEntityType> getEntity() {
        return entity;
    }

    public List<Entity<TransportEntityType>> getTransporters() {
        return transporters;
    }
}
