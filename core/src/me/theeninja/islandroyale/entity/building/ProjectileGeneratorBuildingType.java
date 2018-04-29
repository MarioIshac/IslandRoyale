package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;

public class ProjectileGeneratorBuildingType extends OffenseBuildingType<ProjectileGeneratorBuildingType, InteractableProjectileEntityType> {
    @Override
    public Entity<InteractableProjectileEntityType> produceEntity(InteractableProjectileEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        return new Entity<>(entityType, player, buildingPos);
    }
}
