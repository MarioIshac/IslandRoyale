package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.BuildingConstructor;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.entity.building.Building;
import me.theeninja.islandroyale.entity.building.BuildingType;

public class BuildButton<A extends Building<A, B>, B extends BuildingType<A, B>> extends TextButton {
    private final B buildingEntityType;
    private final Player player;
    private final BuildingConstructor<A, B> buildingConstructor;

    private Vector2 buildPosition;

    public BuildButton(B buildingEntityType, Player player, BuildingConstructor<A, B> buildingConstructor) {
        super(buildingEntityType.getName(), Skins.getInstance().getFlatEarthSkin());

        this.buildingEntityType = buildingEntityType;
        this.player = player;
        this.buildingConstructor = buildingConstructor;

        InputListener buildButtonListener = new BuildButtonListener(this);
        this.addListener(buildButtonListener);
    }

    public A newBuilding(float x, float y, Match match) {
        return getBuildingConstructor().construct(getBuildingType(), getPlayer(), x, y, match);
    }

    public B getBuildingType() {
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

    public BuildingConstructor<A, B> getBuildingConstructor() {
        return buildingConstructor;
    }
}