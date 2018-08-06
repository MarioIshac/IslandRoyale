package me.theeninja.islandroyale.entity.treasure;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

public class ResourceTreasure extends Treasure<ResourceTreasure, ResourceTreasureType> {
    private final int resourceCount;

    public ResourceTreasure(ResourceTreasureType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);

        this.resourceCount = entityType.getRandomResourceCount();
    }

    @Override
    void onTreasureFound(Player player) {
        player.getInventory().put(getEntityType().getResource(), getResourceCount());
    }

    @Override
    protected ResourceTreasure getReference() {
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

    public int getResourceCount() {
        return resourceCount;
    }
}
