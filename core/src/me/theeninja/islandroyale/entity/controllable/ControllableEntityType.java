package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class ControllableEntityType<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InteractableEntityType<A, B> {
    @Override
    public void configureEditor(A entity, Match match) {
        Label targetHeader = new Label(NO_TARGET_COORDS_SIGNAL, Skins.getInstance().getFlatEarthSkin());

        InputListener newTargetListener = new NewTargetListener<>(entity);
        entity.getTargetSelector().addListener(newTargetListener);

        InputListener resetTargetListener = new ResetTargetListener<>(entity);
        entity.getTargetResettor().addListener(resetTargetListener);

        entity.getDescriptor().add(targetHeader).colspan(2).row();
        entity.getDescriptor().add(entity.getTargetSelector());
        entity.getDescriptor().add(entity.getTargetResettor());
    }

    private static final String NO_TARGET_COORDS_SIGNAL = "None";

    /**
     * Represents the amount of tiles per second this entity moves at level 1.
     */
    private float baseMovementSpeed;

    private float baseProductionTime;

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    public float getProductionTime() {
        return baseProductionTime;
    }
}
