package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;

public class InteractableProjectileEntityType extends ControllableEntityType<InteractableProjectileEntityType> {
    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void configureEditor(Entity<InteractableProjectileEntityType> entity, VerticalGroup verticalGroup) {
        super.configureEditor(entity, verticalGroup);
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
    public void present(Entity<InteractableProjectileEntityType> entity, Stage stage) {
        super.present(entity, stage);
    }
}
