package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.ai.Player;

public class HeadquartersCycleListener extends InputListener {
    private final Player player;
    private final int cycleOffset;

    HeadquartersCycleListener(Player player, int cycleOffset) {
        this.player = player;
        this.cycleOffset = cycleOffset;
    }

    private Player getPlayer() {
        return player;
    }

    private int getCycleOffset() {
        return cycleOffset;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        int oldHeadQuartersIndex = getPlayer().getHeadQuartersIndex();

        getPlayer().setHeadQuartersIndex(
            (oldHeadQuartersIndex + getCycleOffset()) % getPlayer().getAllHeadQuarters().size
        );

        return true;
    }
}
