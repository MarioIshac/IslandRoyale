package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntity;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;

public class ProjectileGenerator extends OffenseBuilding<ProjectileGenerator, ProjectileGeneratorType, InteractableProjectileEntity, InteractableProjectileEntityType> {
    public ProjectileGenerator(ProjectileGeneratorType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected ProjectileGenerator getReference() {
        return this;
    }

    @Override
    public Vector2 getAvailableCoordinates(InteractableProjectileEntityType entityType, float buildingX, float buildingY, MatchMap matchMap) {
        return null;
    }

    @Override
    InteractableProjectileEntity newGenericSpecificEntity(InteractableProjectileEntityType entityType, Player owner, float x, float y) {
        return new InteractableProjectileEntity(entityType, owner, x, y);
    }
}
