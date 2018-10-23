package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.entity.EntityType;

public class TransporterType extends ControllableEntityType<Transporter, TransporterType> {
    public static final String CARRIED_ENTITIES_LABEL = "carriedEntities";
    public static final String TRANSPORT_REQUEST_LABEL = "transportRequest";
    public static final String NUMBER_OF_ENTITIES_CARRIED_LABEL = "numberOfEntities";
    private static final String TRANSPORT_ACCEPTOR_LISTENER_LABEL = "transportAcceptorListener";

    /**
     * Represents the number of pixels on the texture's edge that should not be allocated for
     * positioning people on the transport. This to make it so people look "comfortable" placed
     * on a boat.
     */
    private int pixelMargin;

    /**
     * Represents the base maximum capacity of the transporter in regards to transporting {@code Person} entities.
     */
    private int baseMaximumCapacity;

    /**
     * Represents the base vision.
     */
    private int baseRange;

    @Override
    public int getEntityTypeIndex() {
        return TRANSPORTER_TYPE;
    }

    public int getPixelMargin() {
        return pixelMargin;
    }
}