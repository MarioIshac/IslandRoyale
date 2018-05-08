package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TransportInitiatorListener extends InputListener {
    private final Entity<PersonEntityType> entity;
    private final List<Entity<TransportEntityType>> transporters = new ArrayList<>();

    TransportInitiatorListener(Entity<PersonEntityType> entity) {
        this.entity = entity;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Number of transporters " + getTransporters().size());

        for (Entity<TransportEntityType> entity : getTransporters())
            EntityType.setProperty(entity, TransportEntityType.TRANSPORT_REQUEST_LABEL, getEntity());

        System.out.println("Transport has been requested");

        return true;
    }

    public Entity<PersonEntityType> getEntity() {
        return entity;
    }

    public List<Entity<TransportEntityType>> getTransporters() {
        return transporters;
    }
}
