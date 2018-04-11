package me.theeninja.islandroyale.building;

public class ArcherTower extends DefenseBuilding {
    @Override
    public float getBaseDamage() {
        return 10;
    }

    @Override
    public float getBaseFireRate() {
        return 1;
    }

    @Override
    public float getBaseCriticalChance() {
        return 0.1f;
    }

    @Override
    public String getName() {
        return "Archer Tower";
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getTileWidth() {
        return 3;
    }

    @Override
    public int getTileHeight() {
        return 3;
    }

    @Override
    public String getTexturePath() {
        return "defensive/ArcherTower.png";
    }
}
