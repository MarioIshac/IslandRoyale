package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.EntityAttribute;

public class ResourceGenerator extends Building<ResourceGenerator, ResourceGeneratorType> {
    @EntityAttribute
    private float rate;

    public ResourceGenerator(ResourceGeneratorType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected ResourceGenerator getReference() {
        return this;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        super.check(delta, player, matchMap);

        float amountOfResource = delta * getRate();

        player.getInventory().put(getEntityType().getResource(), amountOfResource);
    }

    @Override
    public void present(Camera projector, Stage stage) {
        super.present(projector, stage);
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
