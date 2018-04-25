package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.BuildingEntityType;
import me.theeninja.islandroyale.entity.Entity;

public class BuildButton<T extends BuildingEntityType<T>> extends TextButton {

    private final BuildingEntityType buildingEntityType;

    private Vector2 buildPosition;

    BuildButton(BuildingEntityType buildingEntityType) {
        super(buildingEntityType.getName(), MatchScreen.FLAT_EARTH_SKIN);

        this.buildingEntityType = buildingEntityType;

        setBounds(0, 0, getWidth(), getHeight());

        InputListener buildButtonListener = new BuildButtonListener(this);
        this.addListener(buildButtonListener);
    }

    public Entity<T> newBuilding() {
        return new Entity<T>(getBuildingType());
    }

    public BuildingEntityType getBuildingType() {
        return buildingEntityType;
    }

    public Vector2 getBuildPosition() {
        return buildPosition;
    }

    public void setBuildPosition(Vector2 buildPosition) {
        this.buildPosition = buildPosition;
    }
}