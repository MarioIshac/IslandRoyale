package me.theeninja.islandroyale;

import com.badlogic.gdx.graphics.Texture;

public class IslandTileType {
    public static final float OCEAN_TILE_R = 12;
    public static final float OCEAN_TILE_G = 76;
    public static final float OCEAN_TILE_B = 82;

    public static final int MAX_UNISNGED_BYTE_VALUE = 255;
    public static final float OCEAN_TILE_R_NORMALIZED = OCEAN_TILE_R / MAX_UNISNGED_BYTE_VALUE;
    public static final float OCEAN_TILE_G_NORMALIZED = OCEAN_TILE_G / MAX_UNISNGED_BYTE_VALUE;
    public static final float OCEAN_TILE_B_NORMALIZED = OCEAN_TILE_B / MAX_UNISNGED_BYTE_VALUE;

    public static final byte OCEAN = 0;
    public static final byte DIRT = 1;
    public static final byte GRASS = 2;

    public static final byte END_TILE = GRASS;

    public static final byte TILE_COUNT = END_TILE + 1;

    public static final String[] TEXTURE_PATHS = {
        "tile/Ocean.png",
        "tile/Dirt.png",
        "tile/Grass.png"
    };

    public static boolean isGround(int tile) {
        switch (tile) {
            case DIRT:
            case GRASS:
                return true;
            default:
                return false;
        }
    }
}
