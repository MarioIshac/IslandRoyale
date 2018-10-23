package me.theeninja.islandroyale.entity;

public interface EntityBrand<A extends Entity<A, B, C>, B extends EntityType<A, B, C>, C extends EntityBrand<A, B, C>> {
    int getEntityTypeIndex();
    Class<A> getEntityClass();
    Class<B> getEntityTypeClass();
}
