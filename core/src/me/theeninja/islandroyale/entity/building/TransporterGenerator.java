package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.Transporter;
import me.theeninja.islandroyale.entity.controllable.TransporterType;

public class TransporterGenerator extends OffenseBuilding<TransporterGenerator, TransporterGeneratorType, Transporter, TransporterType> {
    public TransporterGenerator(TransporterGeneratorType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected TransporterGenerator getReference() {
        return this;
    }

    @Override
    public Vector2 getAvailableCoordinates(TransporterType entityType, float buildingX, float buildingY, MatchMap matchMap) {
        Island associatedIsland = matchMap.getIsland(buildingX, buildingY);

        float relativeToIslandX = buildingX - associatedIsland.x;
        float relativeToIslandY = buildingY - associatedIsland.y;

        int availableX = (int) relativeToIslandX;
        int availableY = (int) relativeToIslandY;

        while (associatedIsland.getRepr()[availableX][availableY] != null)
            availableX++;

        return new Vector2(availableX, availableY);
    }

    @Override
    Transporter newGenericSpecificEntity(TransporterType entityType, Player owner, float x, float y) {
        return new Transporter(entityType, owner, x, y);
    }
}
