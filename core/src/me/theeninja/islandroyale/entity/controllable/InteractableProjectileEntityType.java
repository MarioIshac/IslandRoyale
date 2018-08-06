package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntity, InteractableProjectileEntityType> {
    @Override
    public int getDrawingPriority() {
        return EntityType.INTERACTABLE_PROJECTILE_PRIORITY;
    }

    @Override
    public void configureEditor(InteractableProjectileEntity entity) {
        super.configureEditor(entity);
    }
}
