package me.theeninja.islandroyale.building;

import me.theeninja.islandroyale.Resource;

public class LumberMill extends ResourceBuilding {

    LumberMill() {
        getBaseProductionRates().put(Resource.WOOD, 2f);
    }

    @Override
    public String getName() {
        return "Lumber Mill";
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getTileWidth() {
        return 2;
    }

    @Override
    public int getTileHeight() {
        return 2;
    }

    @Override
    public String getTexturePath() {
        return Building.DIRECTORY + ResourceBuilding.DIRECTORY + "LumberMill.png";
    }
}
