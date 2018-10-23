package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class OffenseBuilding<A extends OffenseBuilding<A, B, C, D>, B extends OffenseBuildingType<A, B, C, D>, C extends ControllableEntity<C, D>, D extends ControllableEntityType<C, D>> extends Building<A, B> {
    private Queue<D> entityTypesInQueue;
    private Array<QueueButtonListener<C, D>> queueButtonListeners;
    private Group queueDisplay;

    private float timeUntilProduction = -1;

    @Override
    public void initializeConstructorDependencies() {
        super.initializeConstructorDependencies();

        // By initializing prior to super constructor execution, configureEditor can use this variable
        this.entityTypesInQueue = new Queue<>();
        this.queueButtonListeners = new Array<>();
        this.queueDisplay = new HorizontalGroup();
    }

    public OffenseBuilding(B entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    private void updateQueue() {
        for (QueueButtonListener<C, D> queueButtonListener : getQueueButtonListeners()) {
            if (queueButtonListener.shouldQueryEntity()) {
                D entityTypeProduced = queueButtonListener.getEntityTypeProduced();
                float productionTime = entityTypeProduced.getProductionTime();

                if (getEntityTypesInQueue().size == 0)
                    setTimeUntilProduction(productionTime);

                getEntityTypesInQueue().addLast(entityTypeProduced);

                Texture texture = entityTypeProduced.getTexture();

                Actor entityDisplay = new Image(texture);

                getQueueDisplay().addActor(entityDisplay);

                System.out.println(getQueueDisplay().getStage());
                System.out.println(getQueueDisplay().getHeight() + " " + getQueueDisplay().getWidth() + " <-- Size");

                queueButtonListener.setShouldQueryEntity(false);
            }
        }
    }

    /**
     * Removes the first entity type in queue, while also removing the actor in {@code queueDisplay} that
     * signifies to the user that the entity type is in the queue.
     *
     * @return The first entity type in the queue that was removed.
     */
    private D removeQueueHead() {
        D producedEntityType = getEntityTypesInQueue().removeFirst();

        Actor associatedEntityDisplay = getQueueDisplay().getChildren().get(0);
        // Remove entity display from queue display, signifying that entity is exiting queue and is now being produced
        associatedEntityDisplay.remove();

        return producedEntityType;
    }

    @Override
    public void check(float delta, Player player, Match match) {
        super.check(delta, player, match);

        updateQueue();

        // No entities left to process
        if (getEntityTypesInQueue().size == 0)
            return;

        this.timeUntilProduction -= delta;

        if (getTimeUntilProduction() > 0)
            return;

        D producedEntityType = removeQueueHead();
        C newEntity = produceEntity(producedEntityType, match);

        match.getMatchMap().addEntity(newEntity);

        // No more entities left to process
        if (getEntityTypesInQueue().size == 0)
            return;

        D nextProducedEntityType = getEntityTypesInQueue().first();
        float requiredTime = nextProducedEntityType.getProductionTime();

        setTimeUntilProduction(requiredTime);
    }

    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(projector, hudStage, shapeRenderer);
    }

    public Queue<D> getEntityTypesInQueue() {
        return entityTypesInQueue;
    }

    public float getTimeUntilProduction() {
        return timeUntilProduction;
    }

    public void setTimeUntilProduction(float secondsUntilProduction) {
        this.timeUntilProduction = secondsUntilProduction;
    }

    public Array<QueueButtonListener<C, D>> getQueueButtonListeners() {
        return queueButtonListeners;
    }

    /**
     * Returns the potential coordinates for the entity WITHOUT consideration for surrounding entities at these
     * potential coordinates. Most of the time the potential coordinates would be equivalent to the building
     * coordinates, however transporters must be spawned in the water so that is an exception.
     *
     * @param entityType
     * @param matchMap
     * @return
     */
    public Vector2 getAvailableCoordinates(D entityType, MatchMap matchMap) {
        return new Vector2(getX(), getY());
    }

    public void modifyForSurroundingEntities(Vector2 potentialEntityPosition, Array<Entity<?, ?>>[] entities) {

    }

    abstract C newGenericSpecificEntity(D entityType, Player owner, float x, float y, Match match);

    public C produceEntity(D entityType, Match match) {
        Vector2 availablePosition = getAvailableCoordinates(entityType, match.getMatchMap());
        modifyForSurroundingEntities(availablePosition, match.getMatchMap().getEntities());

        return newGenericSpecificEntity(entityType, getOwner(), availablePosition.x, availablePosition.y, match);
    }

    public Group getQueueDisplay() {
        return queueDisplay;
    }
}
