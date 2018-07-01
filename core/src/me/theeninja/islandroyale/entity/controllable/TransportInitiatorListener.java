package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class TransportInitiatorListener extends InputListener {
    private final Person entity;
    private final Array<Transporter> transporters = new Array<>();

    TransportInitiatorListener(Person entity) {
        this.entity = entity;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


        for (Transporter entity : getTransporters())
            entity.setRequester(getEntity());

        return true;
    }

    public Person getEntity() {
        return entity;
    }

    public Array<Transporter> getTransporters() {
        return transporters;
    }
}
