package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class IdentityInterfacableEntityTypeRegisterer<A extends Entity<A, B>, B extends EntityType<A, B>> extends InterfaceEntityTypeRegisterer<A, B, B> {
    public IdentityInterfacableEntityTypeRegisterer(EntityTypeManager entityTypeManager, Class<B> entityTypeClass, Array<? super B> interfaceStorage, String directory) {
        super(entityTypeManager, entityTypeClass, interfaceStorage, directory);
    }

    @Override
    protected B convert(B entityType) {
        return entityType;
    }
}
