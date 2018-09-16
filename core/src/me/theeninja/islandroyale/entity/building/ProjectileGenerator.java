package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntity;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;
import me.theeninja.islandroyale.gui.screens.Match;

public class ProjectileGenerator extends OffenseBuilding<ProjectileGenerator, ProjectileGeneratorType, InteractableProjectileEntity, InteractableProjectileEntityType> {
    public ProjectileGenerator(ProjectileGeneratorType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected ProjectileGenerator getReference() {
        return this;
    }

    @Override
    InteractableProjectileEntity newGenericSpecificEntity(InteractableProjectileEntityType entityType, Player owner, float x, float y, Match match) {
        return new InteractableProjectileEntity(entityType, owner, x, y, match);
    }
}
