package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.EntityTypeManager;

public class Match {
    private final MatchMap matchMap;
    private final EntityTypeManager entityTypeManager;

    public Match(MatchMap matchMap, EntityTypeManager entityTypeManager) {
        this.matchMap = matchMap;
        this.entityTypeManager  = entityTypeManager;
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }


    public EntityTypeManager getEntityTypeManager() {
        return entityTypeManager;
    }
}
