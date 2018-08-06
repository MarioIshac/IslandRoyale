package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

public class InteractableProjectileEntity extends ControllableEntity<InteractableProjectileEntity, InteractableProjectileEntityType> {
    public InteractableProjectileEntity(InteractableProjectileEntityType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected InteractableProjectileEntity getReference() {
        return this;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        super.check(delta, player, matchMap);
    }

    @Override
    public void present(Camera mapCamera, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(mapCamera, hudStage, shapeRenderer);
    }

    @Override
    protected boolean calculateUpgradable() {
        return false;
    }
}
