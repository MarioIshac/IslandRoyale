package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.BuildingConstructor;
import me.theeninja.islandroyale.entity.EntityTypeManager;
import me.theeninja.islandroyale.entity.EntityTypeRegisterer;
import me.theeninja.islandroyale.entity.InterfaceEntityTypeRegisterer;
import me.theeninja.islandroyale.gui.screens.BuildButton;

public class BuildingTypeRegisterer<A extends Building<A, B>, B extends BuildingType<A, B>> extends InterfaceEntityTypeRegisterer<A, B, BuildButton<? super A, ? super B>> {
    private final Player player;
    private final BuildingConstructor<A, B> buildingConstructor;

    public BuildingTypeRegisterer(EntityTypeManager entityTypeManager, Array<? super BuildButton<? super A, ? super B>> buildButtons, Class<B> entityTypeClass, Player player, BuildingConstructor<A, B> buildingConstructor, String directory) {
        super(entityTypeManager, entityTypeClass, buildButtons, directory);
        this.player = player;
        this.buildingConstructor = buildingConstructor;
    }

    @Override
    protected BuildButton<? super A, ? super B> convert(B entityType) {
        return new BuildButton<>(entityType, getPlayer(), getBuildingConstructor());
    }

    public Player getPlayer() {
        return player;
    }

    public BuildingConstructor<A, B> getBuildingConstructor() {
        return buildingConstructor;
    }
}
