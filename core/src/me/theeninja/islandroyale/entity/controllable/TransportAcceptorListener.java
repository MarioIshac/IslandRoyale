package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class TransportAcceptorListener extends TransportListener<Transporter, TransporterType> {
    TransportAcceptorListener(Transporter entity) {
        super(entity);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Transport accepted");

        Person personRequesting = getEntity().getRequester();

        // Indicates that there is no person requesting to be transported
        if (personRequesting == null)
            return false; // Return false, indicating that other lick events such as show descriptor can be processed

        Transporter currentTransporter = personRequesting.getCarrier();

        // Indicates that the user's intent is to transfer the person from one transproter to ANOTHER transproter
        // which we handle by first removing person from original handler
        if (currentTransporter != null)
            currentTransporter.getCarriedEntities().removeValue(personRequesting, true);

        // Now, old transporter (if any) is irrelevant, and simply add person to new transport
        personRequesting.setCarrier(getEntity());
        getEntity().getCarriedEntities().add(personRequesting);

        // Tell all (including other) transporters that person has been added, therefore other transporters
        // need not listen for person boarding it anymore.
        for (Transporter transporter : getTransporters())
            transporter.setRequester(null);

        return true;
    }
}
