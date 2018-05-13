package me.theeninja.islandroyale.treasure;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.gui.DrawableTexture;

public abstract class Treasure implements DrawableTexture {
    abstract void onTreasureFound(Player player);
}
