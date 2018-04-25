package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

import java.util.List;

public class OffenseBuildingType extends BuildingEntityType<OffenseBuildingType> {
    private Array<? extends MovingEntityType> entityTypesProduced;



    public Array<? extends MovingEntityType> getEntityTypesProduced() {
        return entityTypesProduced;
    }

    @Override
    public void initialize(Entity<BuildingEntityType<OffenseBuildingType>> entity) {

    }

    @Override
    public void check(Entity<BuildingEntityType<OffenseBuildingType>> entity, float delta, Player player, MatchMap matchMap) {

    }

    @Override
    public void present(Entity<BuildingEntityType<OffenseBuildingType>> entity, Batch batch, int centerPixelX, int centerPixelY) {

    }
}
