package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.entity.EntityTypeManager;
import me.theeninja.islandroyale.entity.IdentityInterfacableEntityTypeRegisterer;
import me.theeninja.islandroyale.entity.InterfaceEntityTypeRegisterer;

public class HeadQuartersTyoeRegisterer extends IdentityInterfacableEntityTypeRegisterer<HeadQuarters, HeadQuartersType> {
    public static final int HEAD_QUARTERS_STARTING_INDEX = 0;

    public HeadQuartersTyoeRegisterer(EntityTypeManager entityTypeManager, Array<? super HeadQuartersType> interfaceStorage, Class<HeadQuartersType> entityTypeClass, String directory) {
        super(entityTypeManager, entityTypeClass, interfaceStorage, directory);
    }

    @Override
    protected HeadQuartersType convert(HeadQuartersType entityType) {
        return entityType;
    }
}
