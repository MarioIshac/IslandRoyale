package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Inventory;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.building.HeadQuarters;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public abstract class Player {
    private final Inventory inventory = new Inventory();
    private final Array<HeadQuarters> allHeadQuarters = new Array<>();
    private final String playerName;

    private int x;
    private int y;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public void sendMessage(String message) {

    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * The function that is called when a player requests a transportation route for one of their
     * transporters (entity that transports). This function will eventually have formed an array of 3d vectors
     * that represent the route the transporter will take. Said array will then be passed to a consumer that must
     * handle it.
     *
     * @param entity The entity whose transportation route is being requested.
     * @param matchMap The match map the entity is contained within.
     */
    public abstract void requestTransportationRoute(
        ControllableEntity<?, ?> entity,
        MatchMap matchMap
    );

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Array<HeadQuarters> getAllHeadQuarters() {
        return allHeadQuarters;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPositionOnHeadQuarters(int headQuartersIndex) {
        HeadQuarters headQuarters = getAllHeadQuarters().get(headQuartersIndex);

        int playerX = (int) (headQuarters.getX() - MatchScreen.VISIBLE_WORLD_TILE_WIDTH / 2);
        int playerY = (int) (headQuarters.getY() - MatchScreen.VISIBLE_WORLD_TILE_HEIGHT / 2);

        playerX = Math.max(playerX, 0);
        playerX = Math.min(playerX, MatchScreen.WHOLE_WORLD_TILE_WIDTH);

        playerY = Math.max(playerY, 0);
        playerY = Math.min(playerY, MatchScreen.WHOLE_WORLD_TILE_HEIGHT);

        setX(playerX);
        setY(playerY);
    }
}
