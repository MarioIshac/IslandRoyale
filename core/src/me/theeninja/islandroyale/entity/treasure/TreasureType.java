package me.theeninja.islandroyale.entity.treasure;

import me.theeninja.islandroyale.entity.EntityType;

public class TreasureType<A extends Treasure<A, B>, B extends TreasureType<A, B>> extends EntityType<A, B> {
    @Override
    public int getDrawingPriority() {
        return EntityType.TREASURE_PRIORITY;
    }

    @Override
    protected int getBaseLevel(A entity) {
        return 0;
    }
}
