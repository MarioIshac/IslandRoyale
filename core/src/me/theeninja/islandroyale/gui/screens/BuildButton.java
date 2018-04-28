package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.building.BuildingEntityType;
import me.theeninja.islandroyale.entity.Entity;

public class BuildButton<T extends BuildingEntityType<T>> extends TextButton {

    private final T buildingEntityType;
    private final Player player;

    private Vector2 buildPosition;

    public BuildButton(T buildingEntityType, Player player) {
        super(buildingEntityType.getName(), MatchScreen.FLAT_EARTH_SKIN);

        this.buildingEntityType = buildingEntityType;
        this.player = player;

        setBounds(0, 0, getWidth(), getHeight());

        InputListener buildButtonListener = new BuildButtonListener(this);
        this.addListener(buildButtonListener);
    }

    public Entity<T> newBuilding(Vector2 position) {
        return new Entity<T>(getBuildingType(), getPlayer(), position);
    }

    public T getBuildingType() {
        return buildingEntityType;
    }

    public Vector2 getBuildPosition() {
        return buildPosition;
    }

    public void setBuildPosition(Vector2 buildPosition) {
        this.buildPosition = buildPosition;
    }

    public Player getPlayer() {
        return player;
    }
}