package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.entity.controllable.Transporter;

public abstract class InterfaceEntityTypeRegisterer<A extends Entity<A, B>, B extends EntityType<A, B>, C> extends EntityTypeRegisterer<A, B> {
    private final Array<? super C> interfaceStorage;

    public InterfaceEntityTypeRegisterer(EntityTypeManager entityTypeManager, Class<B> entityTypeClass, Array<? super C> interfaceStorage, String directory) {
        super(entityTypeManager, entityTypeClass, directory);
        this.interfaceStorage = interfaceStorage;
    }

    public Array<? super C> getInterfaceStorage() {
        return interfaceStorage;
    }

    @Override
    protected final void registerEntityType(B entityType) {
        super.registerEntityType(entityType);

        C interfacableItem = convert(entityType);

        getInterfaceStorage().add(interfacableItem);
    }

    protected abstract C convert(B entityType);
}
