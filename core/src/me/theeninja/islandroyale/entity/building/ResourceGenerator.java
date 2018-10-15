package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.EntityAttribute;
import me.theeninja.islandroyale.gui.screens.Match;

public class ResourceGenerator extends Building<ResourceGenerator, ResourceGeneratorType> {
    private static final String BASE_RATE_FIELD_NAME = "baseRate";

    @EntityAttribute(BASE_RATE_FIELD_NAME)
    private float rate;

    public ResourceGenerator(ResourceGeneratorType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);

        setRate(getEntityType().getBaseRate());
    }

    @Override
    protected ResourceGenerator getReference() {
        return this;
    }

    @Override
    public void check(float delta, Player player, Match match) {
        super.check(delta, player, match);

        float amountOfResource = delta * getRate();

        player.getInventory().put(getEntityType().getResource(), amountOfResource);
    }

    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(projector, hudStage, shapeRenderer);
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
