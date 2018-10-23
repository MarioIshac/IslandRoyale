package me.theeninja.islandroyale.entity.bullet;

import me.theeninja.islandroyale.entity.controllable.Person;
import me.theeninja.islandroyale.entity.controllable.PersonType;

public final class PersonBulletProjectileType extends BulletProjectileType<PersonBulletProjectile, PersonBulletProjectileType, Person, PersonType> {
    @Override
    public int getEntityTypeIndex() {
        return PERSON_BULLET_PROJECTILE_TYPE;
    }
}
