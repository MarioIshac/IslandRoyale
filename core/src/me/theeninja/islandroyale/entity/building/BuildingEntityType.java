package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.gui.screens.BuildButton;

public abstract class BuildingEntityType<T extends BuildingEntityType<T>> extends InteractableEntityType<T> {
    private int tileWidth;
    private int tileHeight;
    private int minGroundFiles;

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getMinGroundTiles() {
        return minGroundFiles;
    }
}
