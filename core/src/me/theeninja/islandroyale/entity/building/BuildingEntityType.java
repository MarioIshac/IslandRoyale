package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.gui.screens.BuildButton;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public abstract class BuildingEntityType<T extends BuildingEntityType<T>> extends InteractableEntityType<T> {
    private int minGroundFiles;

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    @Override
    public void present(Entity<T> entity, Stage stage) {
        super.present(entity, stage);
    }
}

/**
 * f(x) = output of mind x
 * f(f(x)) = x
 *
 */
