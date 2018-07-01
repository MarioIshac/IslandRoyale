package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

public abstract class OffenseBuilding<A extends OffenseBuilding<A, B, C, D>, B extends OffenseBuildingType<A, B, C, D>, C extends ControllableEntity<C, D>, D extends ControllableEntityType<C, D>> extends Building<A, B> {
    private final Queue<Integer> entityIdsInQueue = new Queue<>();
    private final Array<QueueButtonListener> queueButtonListeners = new Array<>();
    private float timeUntilProduction = -1;

    public OffenseBuilding(B entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    private void checkQueues() {
        for (QueueButtonListener queueButtonListener : getQueueButtonListeners()) {
            if (queueButtonListener.shouldQueryEntity()) {

                // If there are no other entities queued, queue this entity with its associated time required
                if (getEntityIdsInQueue().size == 0)
                    setTimeUntilProduction(2);

                else {
                    // Else, we still add it, however we do not modify the time as that is being handled by the
                    // below check method
                    getEntityIdsInQueue().addLast(queueButtonListener.getId());
                    queueButtonListener.setShouldQueryEntity(false);
                }
            }

        }
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        super.check(delta, player, matchMap);

        checkQueues();

        // No entities left to process
        if (getEntityIdsInQueue().size == 0)
            return;

        this.timeUntilProduction -= delta;

        if (getTimeUntilProduction() > 0)
            return;

        int entityTypeID = getEntityIdsInQueue().removeFirst();

        D producedEntityType = InteractableEntityType.getEntityType(entityTypeID);

        Vector2 position = new Vector2(getSprite().getX(), getSprite().getY());
        C newEntity = newEntity(producedEntityType, matchMap);

        matchMap.getEntities().add(newEntity);

        // No more entities left to process
        if (getEntityIdsInQueue().size == 0)
            return;

        int nextEntityID = getEntityIdsInQueue().first();
        float requiredTime = 2;

        setTimeUntilProduction(requiredTime);
    }

    @Override
    public void present(Camera projector, Stage stage) {
        super.present(projector, stage);
    }

    public Queue<Integer> getEntityIdsInQueue() {
        return entityIdsInQueue;
    }

    public float getTimeUntilProduction() {
        return timeUntilProduction;
    }

    public void setTimeUntilProduction(float secondsUntilProduction) {
        this.timeUntilProduction = secondsUntilProduction;
    }

    public Array<QueueButtonListener> getQueueButtonListeners() {
        return queueButtonListeners;
    }

    public abstract Vector2 getAvailableCoordinates(D entityType, float buildingX, float buildingY, MatchMap matchMap);
    abstract C newGenericSpecificEntity(D entityType, Player owner, float x, float y);

    public C newEntity(D entityType, MatchMap matchMap) {
        Vector2 availablePosition = getAvailableCoordinates(entityType, getSprite().getX(), getSprite().getY(), matchMap);

        return newGenericSpecificEntity(entityType, getOwner(), availablePosition.x, availablePosition.y);
    }
}
