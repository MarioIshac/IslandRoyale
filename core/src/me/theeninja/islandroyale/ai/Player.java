package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector3;
import me.theeninja.islandroyale.Inventory;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import java.util.function.Consumer;

public abstract class Player {
    private final Inventory inventory = new Inventory();
    private final Island mainIsland;

    public Player(Island mainIsland) {
        this.mainIsland = mainIsland;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Island getMainIsland() {
        return mainIsland;
    }

    /**
     * The function that is called when a player requests a transportation route for one of their
     * transporters (entity that transports). This function will eventually have formed an array of 3d vectors
     * that represent the route the transporter will take. Said array will then be passed to a consumer that must
     * handle it.
     *
     * @param entity The entity whose transportation route is being requested.
     * @param matchMap The match map the entity is contained within.
     * @param postAcceptanceConsumer The callback function to call after the request has been successfully
     *                               returned (this is not instanteous, there may be an unintentional (human) delay
     *                               or intentional delay hence the need for a callback)
     */
    public abstract void requestTransportationRoute(
        ControllableEntity<?, ?> entity,
        MatchMap matchMap,
        Consumer<Vector3[]> postAcceptanceConsumer
    );
}
