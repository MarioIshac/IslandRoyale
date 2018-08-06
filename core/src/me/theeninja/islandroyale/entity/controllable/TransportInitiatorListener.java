package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class TransportInitiatorListener extends TransportListener<Person, PersonType> {
    TransportInitiatorListener(Person entity) {
        super(entity);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Requesting transport");

        for (Transporter entity : getTransporters())
            entity.setRequester(getEntity());

        return true;
    }
}
