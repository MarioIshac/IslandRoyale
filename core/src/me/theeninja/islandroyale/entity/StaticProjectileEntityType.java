package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.building.DefenseBuildingType;

public class StaticProjectileEntityType extends EntityType<StaticProjectileEntityType> {

    public static final String INITIATOR_LABEL = "initiator";
    public static final String TARGET_LABEL = "target";
    public static final String HAS_COLLIDED_LABEL = "hasCollided";

    public void externalInitialize(Entity<StaticProjectileEntityType> proj,
                                   Entity<DefenseBuildingType> shooter,
                                   Entity<? extends InteractableEntityType<?>> target) {
        setProperty(proj, INITIATOR_LABEL, shooter);
        setProperty(proj, TARGET_LABEL, target);
    }

    @Override
    public void setUp(Entity<StaticProjectileEntityType> entity) {
        setProperty(entity, HAS_COLLIDED_LABEL, false);
    }

    @Override
    public boolean shouldRemove(Entity<StaticProjectileEntityType> entity) {
        return getProperty(entity, HAS_COLLIDED_LABEL);
    }

    @Override
    public void check(Entity<StaticProjectileEntityType> entity, float delta, Player player, MatchMap matchMap) {
        Entity<DefenseBuildingType> shooter = getProperty(entity, INITIATOR_LABEL);
        Entity<? extends InteractableEntityType<?>> otherEntity = getProperty(entity, TARGET_LABEL);

        // The collision check is done with respect to pixels
        float otherLowerAbsolutePixelX = (otherEntity.getSprite().getX());
        float otherLowerAbsolutePixelY = (otherEntity.getSprite().getY());

        float thisLowerAbsolutePixelX = (entity.getSprite().getX());
        float thisLowerAbsolutePixelY = (entity.getSprite().getY());

        Rectangle otherRect = new Rectangle(otherLowerAbsolutePixelX, otherLowerAbsolutePixelY, otherEntity.getType().getTexture().getWidth() / 16f, otherEntity.getType().getTexture().getHeight() / 16f);
        Rectangle thisRect = new Rectangle(thisLowerAbsolutePixelX, thisLowerAbsolutePixelY, entity.getType().getTexture().getWidth() / 16f, entity.getType().getTexture().getHeight() / 16f);

        System.out.println("Has it hit?");
        if (otherRect.contains(thisRect)) {
            System.out.println("It has hit");

            setProperty(entity, HAS_COLLIDED_LABEL, true);
            changeProperty(otherEntity, InteractableEntityType.HEALTH_LABEL, (Float health) -> health -= shooter.getType().getBaseDamage());

            return;
        }

        Vector2 otherCenter = otherRect.getCenter(new Vector2());
        Vector2 thisCenter = thisRect.getCenter(new Vector2());

        float yDistance = otherCenter.y - thisCenter.y;
        float xDistance = otherCenter.x - thisCenter.x;

        // Arc tangent only returns valid value in quadrant 1 or 4, i.e other entity has to be
        // to the right of this entity. Solution is two if statements below
        float resultingAngle = (float) Math.atan(yDistance / xDistance);

        // Shift from 1st to 3rd quadrant OR 4th to 2nd quadrant (handles both cases).
        if (xDistance < 0)
            resultingAngle += Math.PI;

        float currentSpeed = entity.getVelocityPerSecond().x;

        entity.getVelocityPerSecond().set(currentSpeed, (float) (resultingAngle));
        entity.getSprite().setRotation((float) Math.toDegrees(resultingAngle) - 45);
    }

    @Override
    public void present(Entity<StaticProjectileEntityType> entity, Stage stage) {

    }
}
