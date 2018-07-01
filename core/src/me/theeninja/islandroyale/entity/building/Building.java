package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.InteractableEntity;

public abstract class Building<A extends Building<A, B>, B extends BuildingType<A, B>> extends InteractableEntity<A, B> {
    public Building(B entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }
}
