package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntityType> {
    @Override
    public int getDrawingPriority() {
        return 3;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void configureEditor(Entity<InteractableProjectileEntityType> entity, Table table) {
        super.configureEditor(entity, table);
    }

    @Override
    public void setUp(Entity<InteractableProjectileEntityType> entity) {
        super.setUp(entity);
    }

    @Override
    public void check(Entity<InteractableProjectileEntityType> entity, float delta, Player player, MatchMap matchMap) {
        setDefaultSpeed(entity);
    }

    @Override
    public void present(Entity<InteractableProjectileEntityType> entity, Camera mapCamera, Stage hudStage) {
        super.present(entity, mapCamera, hudStage);
    }
}
