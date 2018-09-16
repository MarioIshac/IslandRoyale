package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class Building<A extends Building<A, B>, B extends BuildingType<A, B>> extends InteractableEntity<A, B> {
    public Building(B entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected boolean calculateUpgradable() {
        // A building is always upgradable
        return true;
    }
}
