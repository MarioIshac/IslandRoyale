package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.gui.screens.Match;

import java.lang.annotation.Documented;

public abstract class BuildingType<A extends Building<A, B>, B extends BuildingType<A, B>> extends InteractableEntityType<A, B> {
    private int minGroundFiles;

    public int getMinGroundTiles() {
        return minGroundFiles;
    }

    @Override
    public void configureEditor(A entity, Match match) {
        TextButton sellButton = new TextButton("Sell", Skins.getInstance().getFlatEarthSkin());

        UserRemovalListener<A, B> userRemovalListener = new UserRemovalListener<>(entity);
        sellButton.addListener(userRemovalListener);

        entity.getDescriptor().add(sellButton).row();
    }
}
