package me.theeninja.islandroyale.entity.bullet;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.Person;
import me.theeninja.islandroyale.entity.controllable.PersonType;

public final class PersonBulletProjectile extends BulletProjectile<PersonBulletProjectile, PersonBulletProjectileType, Person, PersonType> {
    public PersonBulletProjectile(PersonBulletProjectileType entityType, Player owner, float x, float y, Person entityFrom) {
        super(entityType, owner, x, y, entityFrom);
    }

    @Override
    protected PersonBulletProjectile getReference() {
        return this;
    }
}
