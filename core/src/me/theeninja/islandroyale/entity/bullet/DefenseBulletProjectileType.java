package me.theeninja.islandroyale.entity.bullet;

import me.theeninja.islandroyale.entity.building.DefenseBuilding;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;

public final class DefenseBulletProjectileType extends BulletProjectileType<DefenseBulletProjectile, DefenseBulletProjectileType, DefenseBuilding, DefenseBuildingType> {
    @Override
    public int getEntityTypeIndex() {
        return DEFENSE_BULLET_PROJECTILE_TYPE;
    }
}
