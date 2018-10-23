package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.gui.screens.Match;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntity, InteractableProjectileEntityType> {
    @Override
    public int getEntityTypeIndex() {
        return INTERACTABLE_PROJECTILE_ENTITY_TYPE;
    }
}
