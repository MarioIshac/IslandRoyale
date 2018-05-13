package me.theeninja.islandroyale.treasure;

import me.theeninja.islandroyale.ai.Player;

public class DataTreasure extends Treasure {

    private final String message;

    DataTreasure(String message) {
        this.message = message;
    }

    @Override
    void onTreasureFound(Player player) {

    }

    @Override
    public String getTexturePath() {
        return "treasure/TreasureNote.png";
    }

    public String getMessage() {
        return message;
    }
}
