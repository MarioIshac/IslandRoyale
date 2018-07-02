package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.EntityAttribute;
import me.theeninja.islandroyale.entity.Entity;

public class Transporter extends ControllableEntity<Transporter, TransporterType> {
    @EntityAttribute
    private int maximumCapacity;

    private final TransportAcceptorListener transportAcceptorListener = new TransportAcceptorListener(this);
    private final Array<Person> carriedEntities = new Array<>();

    private Person requester;

    public Transporter(TransporterType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);

        this.addListener(getTransportAcceptorListener());
    }

    @Override
    protected Transporter getReference() {
        return this;
    }

    public TransportAcceptorListener getTransportAcceptorListener() {
        return transportAcceptorListener;
    }

    public Array<Person> getCarriedEntities() {
        return carriedEntities;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        super.check(delta, player, matchMap);

        // Update all transporters within the load to transport listener.
        getTransportAcceptorListener().getTransporters().clear();

        for (Entity<?, ?> matchMapEntity : matchMap.getEntities()) {
            if (!(matchMapEntity.getEntityType() instanceof TransporterType))
                continue;

            Transporter transporter = (Transporter) matchMapEntity;

            transportAcceptorListener.getTransporters().add(transporter);
        }

        updateMoveAttributes();

        int collectiveTotal = 0;

        float positionOffset = getEntityType().getPixelMargin() / 16f;

        for (Person person : getCarriedEntities()) {
            float personXPos = positionOffset + getSprite().getX() + collectiveTotal;
            float personYPos = positionOffset + getSprite().getY();

            person.getSprite().setPosition(personXPos, personYPos);
            collectiveTotal += person.getSprite().getWidth();
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
}
