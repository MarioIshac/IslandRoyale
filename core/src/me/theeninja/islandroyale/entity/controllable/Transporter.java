package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityAttribute;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.treasure.Treasure;
import me.theeninja.islandroyale.gui.screens.Match;

public class Transporter extends ControllableEntity<Transporter, TransporterType> {
    private static final String BASE_MAXIMUM_CAPACITY_FIELD_NAME = "baseMaximumCapacity";
    private static final String BASE_RANGE_FIELD_NAME = "baseRange";

    @EntityAttribute(BASE_MAXIMUM_CAPACITY_FIELD_NAME)
    private int maximumCapacity;

    @EntityAttribute(BASE_RANGE_FIELD_NAME)
    private int range;

    private TransportAcceptorListener transportListener;
    private Array<Person> carriedEntities;

    private Person requester;

    @Override
    public void initializeConstructorDependencies() {
        super.initializeConstructorDependencies();

        this.transportListener = new TransportAcceptorListener(this);
        this.carriedEntities = new Array<>();
    }

    public Transporter(TransporterType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);

        this.addListener(getTransportListener());
    }

    @Override
    protected Transporter getReference() {
        return this;
    }

    public TransportAcceptorListener getTransportListener() {
        return transportListener;
    }

    public Array<Person> getCarriedEntities() {
        return carriedEntities;
    }

    @Override
    public void check(float delta, Player player, Match match) {
        super.check(delta, player, match);

        getTransportListener().refreshTransporters(match.getMatchMap());

        updateMoveAttributes();

        int collectiveTotal = 0;

        float positionOffset = getEntityType().getPixelMargin() / 16f;

        for (Person person : getCarriedEntities()) {
            float personXPos = positionOffset + getX() + collectiveTotal;
            float personYPos = positionOffset + getY();

            person.setPosition(personXPos, personYPos);
            collectiveTotal += person.getWidth();
        }
    }

    public Person getRequester() {
        return requester;
    }

    public void setRequester(Person requester) {
        this.requester = requester;
    }

    @Override
    public void present(Camera mapCamera, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(mapCamera, hudStage, shapeRenderer);
    }

    @Override
    protected boolean calculateUpgradable() {
        return false;
    }
}
