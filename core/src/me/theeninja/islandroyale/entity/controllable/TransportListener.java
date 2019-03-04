package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.EntityListener;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.EntityType;

abstract class TransportListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends EntityListener<A, B> {
    private final Array<Transporter> transporters = new Array<>();

    TransportListener(A entity) {
        super(entity);
    }

    private static final int TRANSPORTER_TYPE_KEY = EntityType.Unsafe.getEntityTypeKey(TransporterType.class);

    void refreshTransporters(MatchMap matchMap) {
        getTransporters().clear();

        Array<Transporter> currentTransporters = matchMap.getEntitiesOfTypeUnsafely(TRANSPORTER_TYPE_KEY);

        getTransporters().addAll(currentTransporters);
    }

    Array<Transporter> getTransporters() {
        return transporters;
    }
}
