package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ProjectileEntityType;

public class ProjectileGeneratorBuildingType extends OffenseBuildingType<ProjectileGeneratorBuildingType, ProjectileEntityType> {
    @Override
    public Entity<ProjectileEntityType> getEntityRequested(ProjectileEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        return new Entity<>(entityType, player, buildingPos);
    }
}
