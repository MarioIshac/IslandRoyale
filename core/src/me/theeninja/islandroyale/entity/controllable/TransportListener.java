package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.EntityListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public abstract class TransportListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends EntityListener<A, B> {
    private final Array<Transporter> transporters = new Array<>();

    protected TransportListener(A entity) {
        super(entity);
    }

    void refreshTransporters(Array<Entity<?, ?>>[] matchMapEntities) {
        getTransporters().clear();

        for (int entityPriority = 0; entityPriority < EntityType.NUMBER_OF_PRIORITIES; entityPriority++) {
            Array<Entity<?, ?>> priorityEntities = matchMapEntities[entityPriority];

            for (Entity<?, ?> matchMapEntity : priorityEntities) {
                if (!(matchMapEntity instanceof Transporter))
                    continue;

                Transporter transporter = (Transporter) matchMapEntity;

                getTransporters().add(transporter);
            }
        }
    }

    Array<Transporter> getTransporters() {
        return transporters;
    }
}
