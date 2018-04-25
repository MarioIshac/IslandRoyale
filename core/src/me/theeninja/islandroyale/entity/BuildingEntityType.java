package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandRoyaleGame;
import me.theeninja.islandroyale.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//                                      Required that subclass T specifies type T
//
public abstract class BuildingEntityType<T extends BuildingEntityType<T>> extends EntityType<BuildingEntityType<T>> {
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

    public static Array<? extends BuildingEntityType> loadBuildingEntityTypes() {
        Array<BuildingEntityType> buildingTypes = new Array<>();

        Array<DefenseBuildingType> defenseBuildingTypes = EntityType.loadEntityTypes(DefenseBuildingType.class);
        Array<OffenseBuildingType> offenseBuildingTypes = EntityType.loadEntityTypes(OffenseBuildingType.class);
        Array<ResourceBuildingType> resourceBuildingTypes = EntityType.loadEntityTypes(ResourceBuildingType.class);

        buildingTypes.addAll(defenseBuildingTypes);
        buildingTypes.addAll(offenseBuildingTypes);
        buildingTypes.addAll(resourceBuildingTypes);

        return buildingTypes;
    }
}
