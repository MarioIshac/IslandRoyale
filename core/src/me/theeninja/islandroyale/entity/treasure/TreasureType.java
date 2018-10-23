package me.theeninja.islandroyale.entity.treasure;

import me.theeninja.islandroyale.entity.EntityType;

public abstract class TreasureType<A extends Treasure<A, B>, B extends TreasureType<A, B>> extends EntityType<A, B> {
    @Override
    protected int getBaseLevel(A entity) {
        return 0;
    }
}
