package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntity;
import me.theeninja.islandroyale.entity.controllable.InteractableProjectileEntityType;

public class ProjectileGeneratorType extends OffenseBuildingType<ProjectileGenerator, ProjectileGeneratorType, InteractableProjectileEntity, InteractableProjectileEntityType> {
    @Override
    public int getEntityTypeIndex() {
        return PROJECTILE_GENERATOR_TYPE;
    }
}
