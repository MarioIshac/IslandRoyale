package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.gui.screens.Match;

public class PersonType extends ControllableEntityType<Person, PersonType> {
    public static final String PERSON_DIRECTORY = "person/";

    private float baseDamage;
    private float baseFireRate;

    @Override
    public int getDrawingPriority() {
        return EntityType.PERSON_PRIORITY;
    }

    @Override
    public void configureEditor(Person entity, Match match) {
        super.configureEditor(entity, match);

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
