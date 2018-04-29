package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import java.util.LinkedList;

/**
 * @param <T> the <b>t</b>ype of the class that is subclassing OffensiveBuildingType
 * @param <P> the entity type that this offense building <b>p</b>roduces (most likely between
 *           {@link me.theeninja.islandroyale.entity.controllable.PersonEntityType} and
 *           {@link me.theeninja.islandroyale.entity.controllable.TransportEntityType}
 */
public abstract class OffenseBuildingType<T extends OffenseBuildingType<T, P>, P extends ControllableEntityType<P>> extends BuildingEntityType<T> {
    private Array<Integer> entityIDsProduced;

    private static final String ENTITY_IDS_IN_QUEUE = "entityIDs";
    private static final String TIMER_LABEL = "timer";

    @Override
    public void initialize(Entity<T> entity) {
        super.initialize(entity);
        setProperty(entity, ENTITY_IDS_IN_QUEUE, new LinkedList<Integer>());
    }

    private static int i = 1;

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        LinkedList<Integer> entityIDs = getProperty(entity, ENTITY_IDS_IN_QUEUE);



        // No entities left to process
        if (entityIDs.isEmpty())
            return;

        float secondsLeft = getProperty(entity, TIMER_LABEL);
        secondsLeft -= delta;

        if (secondsLeft > 0) {
            setProperty(entity, TIMER_LABEL, secondsLeft);
            return;
        }

        int entityTypeID = entityIDs.pollFirst();
        P producedEntityType = InteractableEntityType.getEntityType(entityTypeID);

        Entity<? extends InteractableEntityType<?>> newEntity = produceEntity(producedEntityType, player, entity.getPos(), matchMap);

        // No more entities left to process
        if (entityIDs.isEmpty())
            return;

        int nextEntityID = entityIDs.pollFirst();
        float requiredTime = 10;

        setProperty(entity, TIMER_LABEL, requiredTime);
    }

    public abstract Entity<P> produceEntity(P entityType, Player player, Vector2 buildingPos, MatchMap matchMap);

    @Override
    public void present(Entity<T> entity, Batch batch, float centerPixelX, float centerPixelY) {

    }

    public Array<Integer> getEntityIDsProduced() {
        return entityIDsProduced;
    }
}
