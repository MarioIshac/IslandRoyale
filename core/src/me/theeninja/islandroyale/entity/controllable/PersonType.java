package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.*;

public class PersonType extends ControllableEntityType<Person, PersonType> {
    public static final String PERSON_DIRECTORY = "person/";

    private float baseDamage;
    private float baseFireRate;

    public static final String CARRIER_LABEL = "carrier";

    private final static String TIME_LEFT_LABEL = "timeLeft";

    private final static String LOAD_TO_TRANSPORT_LISTENER_LABEL = "loadToTransportLabel";

    @Override
    public int getDrawingPriority() {
        return EntityType.PERSON_PRIORITY;
    }

    @Override
    public void configureEditor(Person entity) {
        super.configureEditor(entity);

        TextButton transportButton = new TextButton("Transport", Skins.getInstance().getFlatEarthSkin());
        transportButton.addListener(entity.getTransportListener());

        entity.getDescriptor().add(transportButton).row();
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getBaseFireRate() {
        return baseFireRate;
    }
}
