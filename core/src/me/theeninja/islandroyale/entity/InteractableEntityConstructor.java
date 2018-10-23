package me.theeninja.islandroyale.entity;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.building.Building;
import me.theeninja.islandroyale.entity.building.BuildingType;
import me.theeninja.islandroyale.gui.screens.Match;

@FunctionalInterface
public interface InteractableEntityConstructor<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> {
    A construct(B entityType, Player player, float x, float y, Match match);
}
