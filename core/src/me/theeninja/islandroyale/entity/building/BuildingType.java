package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.entity.UserRemovalListener;

public abstract class BuildingType<A extends Building<A, B>, B extends BuildingType<A, B>> extends InteractableEntityType<A, B> {
    private int minGroundFiles;

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    @Override
    public int getDrawingPriority() {
        return EntityType.BUILDING_PRIORITY;
    }

    @Override
    public void configureEditor(A entity) {
        TextButton sellButton = new TextButton("Sell", Skins.getInstance().getFlatEarthSkin());

        UserRemovalListener<A, B> userRemovalListener = new UserRemovalListener<>(entity);
        sellButton.addListener(userRemovalListener);

        entity.getDescriptor().add(sellButton).row();
    }
}
