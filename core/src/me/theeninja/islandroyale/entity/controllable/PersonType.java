package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;
import me.theeninja.islandroyale.gui.screens.Match;

public final class PersonType extends ControllableEntityType<Person, PersonType> implements AttackerType<PersonBulletProjectile, PersonBulletProjectileType, Person, PersonType>{
    public static final String PERSON_DIRECTORY = "person/";

    private float baseRange;
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

    @Override
    public float getBaseRange() {
        return this.baseRange;
    }

    @Override
    public float getBaseFireRate() {
        return this.baseFireRate;
    }

    @Override
    public float getBaseDamage() {
        return this.baseDamage;
    }
}
