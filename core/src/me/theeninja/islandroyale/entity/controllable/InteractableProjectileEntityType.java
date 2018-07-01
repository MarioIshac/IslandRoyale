package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntity, InteractableProjectileEntityType> {
    @Override
    public int getDrawingPriority() {
        return 3;
    }

    @Override
    public void configureEditor(InteractableProjectileEntity entity, Table table) {
        super.configureEditor(entity, table);
    }
}
