package me.theeninja.islandroyale.entity.treasure;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class Treasure<A extends Treasure<A, B>, B extends TreasureType<A, B>> extends Entity<A, B> {
    public Treasure(B entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    abstract void onTreasureFound(Player player);

    private boolean isFound;

    @Override
    public void check(float delta, Player player, Match match) {
        for (int entityPriorityLevel = EntityType.TREASURE_SEEKER_PRIORITY_MIN; entityPriorityLevel < EntityType.TREASURE_SEEKER_PRIORITY_MAX; entityPriorityLevel++) {
            Array<Entity<?, ?>> priorityEntities = match.getMatchMap().getCertainPriorityEntities(entityPriorityLevel);

            for (Entity<?, ?> receivingEntity : priorityEntities)
                if (overlap(this, receivingEntity))
                    setFound(true);
        }
    }

    public void setFound(boolean found) {
        this.isFound = found;
    }

    @Override
    public boolean shouldRemove() {
        return this.isFound;
    }
}
