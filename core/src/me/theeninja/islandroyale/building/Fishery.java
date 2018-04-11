package me.theeninja.islandroyale.building;

public class Fishery extends ResourceBuilding {
    @Override
    public String getName() {
        return "Fishery";
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
    public int getMinGroundTiles() {
        return 2;
    }

    @Override
    public String getTexturePath() {
        return "building/resource/fishering.png";
    }
}
