package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.gui.screens.Match;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntity, InteractableProjectileEntityType> {
    @Override
    public int getDrawingPriority() {
        return EntityType.INTERACTABLE_PROJECTILE_PRIORITY;
    }

    @Override
    public void configureEditor(InteractableProjectileEntity entity, Match match) {
        super.configureEditor(entity, match);
    }
}
