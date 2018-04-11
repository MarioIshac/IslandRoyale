package me.theeninja.islandroyale;

import com.badlogic.gdx.graphics.Texture;

public enum IslandTileType {
    DIRT("tile/Dirt.png"),
    N_GRASS(null),
    E_GRASS(null),
    S_GRASS(null),
    W_GRASS(null),
    NE_U_GRASS(null),
    SE_U_GRASS(null),
    NW_U_GRASS(null),
    SW_U_GRASS(null),
    NE_I_GRASS(null),
    SE_I_GRASS(null),
    NW_I_GRASS(null),
    SW_I_GRASS(null);

    private final String texturePath;

    private IslandTileType(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
