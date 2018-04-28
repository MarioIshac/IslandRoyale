package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.gui.screens.BuildButton;

public abstract class BuildingEntityType<T extends BuildingEntityType<T>> extends EntityType<T> {
    public static final String BUILDING_DIRECTORY = "building/";

    private int tileWidth;
    private int tileHeight;
    private int minGroundFiles;
    private int cost;

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    public int getCost() {
        return cost;
    }


    public static <T extends BuildingEntityType<T>> Array<BuildButton<T>> newBuildButtons(Array<T> types, Player player) {
        Array<BuildButton<T>> buildButtons = new Array<>();

        for (T type : types) {
            buildButtons.add(new BuildButton<>(type, player));
        }

        return buildButtons;
    }
}
