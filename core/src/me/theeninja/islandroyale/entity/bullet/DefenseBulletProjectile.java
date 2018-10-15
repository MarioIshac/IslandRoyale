package me.theeninja.islandroyale.entity.bullet;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.building.DefenseBuilding;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;

public final class DefenseBulletProjectile extends BulletProjectile<DefenseBulletProjectile, DefenseBulletProjectileType, DefenseBuilding, DefenseBuildingType> {
    public DefenseBulletProjectile(DefenseBulletProjectileType entityType, Player owner, float x, float y, DefenseBuilding entityFrom) {
        super(entityType, owner, x, y, entityFrom);
    }

    @Override
    protected DefenseBulletProjectile getReference() {
        return this;
    }
}
