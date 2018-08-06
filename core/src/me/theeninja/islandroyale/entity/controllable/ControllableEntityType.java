package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.gui.screens.MatchScreen;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public abstract class ControllableEntityType<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InteractableEntityType<A, B> {
    @Override
    public void configureEditor(A entity) {
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
