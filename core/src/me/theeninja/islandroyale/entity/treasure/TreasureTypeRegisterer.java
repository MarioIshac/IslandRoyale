package me.theeninja.islandroyale.entity.treasure;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.entity.EntityTypeManager;
import me.theeninja.islandroyale.entity.IdentityInterfacableEntityTypeRegisterer;

public class TreasureTypeRegisterer<A extends Treasure<A, B>, B extends TreasureType<A, B>> extends IdentityInterfacableEntityTypeRegisterer<A, B> {
    public TreasureTypeRegisterer(EntityTypeManager entityTypeManager, Array<? super B> interfaceStorage, Class<B> entityTypeClass, String directory) {
        super(entityTypeManager, entityTypeClass, interfaceStorage, directory);
    }
}
