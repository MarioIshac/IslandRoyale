package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Skins;

public abstract class BuildingType<A extends Building<A, B>, B extends BuildingType<A, B>> extends InteractableEntityType<A, B> {
    private int minGroundFiles;

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    @Override
    public int getDrawingPriority() {
        return 0;
    }

    @Override
    public void configureEditor(A entity, Table table) {
        TextButton sellButton = new TextButton("Sell", Skins.getInstance().getFlatEarthSkin());
        table.add(sellButton).row();
    }
}
