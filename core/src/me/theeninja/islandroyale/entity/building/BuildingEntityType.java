package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public abstract class BuildingEntityType<T extends BuildingEntityType<T>> extends InteractableEntityType<T> {
    private int minGroundFiles;

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    @Override
    public int getDrawingPriority() {
        return 0;
    }

    @Override
    public void present(Entity<T> entity, Camera projector, Stage stage) {
        super.present(entity, projector, stage);
    }

    @Override
    public void configureEditor(Entity<T> entity, Table table) {
        TextButton sellButton = new TextButton("Sell", MatchScreen.FLAT_EARTH_SKIN);
        table.add(sellButton).row();
    }
}
