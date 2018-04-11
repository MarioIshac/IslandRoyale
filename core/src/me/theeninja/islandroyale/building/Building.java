package me.theeninja.islandroyale.building;

import me.theeninja.islandroyale.gui.DrawableTexture;

public abstract class Building implements DrawableTexture {
    public static final String DIRECTORY = "building/";

    /**
     * @return the name of this building that will be shown during information views (such as menus) to the user
     */
    public abstract String getName();

    /**
     * @return the max level that this building can be upgraded to (starting from level 1)
     */
    public abstract int getMaxLevel();

    public abstract int getTileWidth();
    public abstract int getTileHeight();

    /**
     * This method should be overrided in the case where a portion of the building can be placed on water.
     *
     * @return how many ground tiles a building must be touching in order to have a valid placement
     *         at that area
     */
    public int getMinGroundTiles() {
        return getTileWidth() * getTileHeight();
    }
}
