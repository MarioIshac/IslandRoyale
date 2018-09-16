package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.gui.screens.Match;

public class HeadQuarters extends Building<HeadQuarters, HeadQuartersType> {
    public HeadQuarters(HeadQuartersType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected HeadQuarters getReference() {
        return this;
    }
}
