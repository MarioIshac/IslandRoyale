package me.theeninja.islandroyale.entity;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.building.Building;
import me.theeninja.islandroyale.entity.building.BuildingType;

@FunctionalInterface
public interface BuildingConstructor<A extends Building<A, B>, B extends BuildingType<A, B>> {
    A construct(B entityType, Player player, float x, float y);
}
