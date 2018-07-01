package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class TransportAcceptorListener extends InputListener {
    private final Transporter transporter;
    private final Array<Transporter> transporters = new Array<>();

    TransportAcceptorListener(Transporter transporter) {
        this.transporter = transporter;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Person personRequesting = getTransporter().getRequester();

        // Indicates that there is no person requesting to be transported
        if (personRequesting == null)
            return false; // Return false, indicating that other lick events such as show descriptor can be processed

        Transporter currentTransporter = personRequesting.getCarrier();

        // Indicates that the user's intent is to transfer the person from one transproter to ANOTHER transproter
        // which we handle by first removing person from original handler
        if (currentTransporter != null)
            currentTransporter.getCarriedEntities().removeValue(personRequesting, true);

        // Now, old transporter (if any) is irrelevant, and simply add person to new transport
        personRequesting.setCarrier(getTransporter());
        getTransporter().getCarriedEntities().add(personRequesting);

        // Tell all (including other) transporters that person has been added, therefore other transporters
        // need not listen for person boarding it anymore.
        for (Transporter transporter : getTransporters())
            transporter.setRequester(null);

        return true;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public Array<Transporter> getTransporters() {
        return transporters;
    }
}
