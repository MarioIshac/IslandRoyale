package me.theeninja.islandroyale.entity;

import me.theeninja.islandroyale.entity.bullet.BulletProjectile;
import me.theeninja.islandroyale.entity.bullet.BulletProjectileType;

public interface AttackerType<A extends BulletProjectile<A, B, C, D>, B extends BulletProjectileType<A, B, C, D>, C extends InteractableEntity<C, D> & Attacker<A, B, C, D>, D extends InteractableEntityType<C, D> & AttackerType<A, B, C, D>> {
    float getBaseRange();
    float getBaseFireRate();
    float getBaseDamage();
}
