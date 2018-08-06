package me.theeninja.islandroyale.entity.treasure;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

public class DataTreasure extends Treasure<DataTreasure, DataTreasureType> {
    public DataTreasure(DataTreasureType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    void onTreasureFound(Player player) {

    }

    @Override
    protected DataTreasure getReference() {
        return this;
    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {

    }

    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {

    }
}
