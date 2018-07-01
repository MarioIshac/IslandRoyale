package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

public abstract class InteractableEntity<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends Entity<A, B> {
    public InteractableEntity(B entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @EntityAttribute
    private float health;

    private final Table descriptor = new Table();
    private boolean isDescriptorShown = false;

    @Override
    public boolean shouldRemove() {
        return false;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        //if (shouldRemove(entity))
            //  setProperty(entity, DESCRIPTOR_SHOWN_LABEL, false);
    }

    @Override
    public void present(Camera projector, Stage stage) {
        if (isDescriptorShown()) {
            Vector3 coords = new Vector3(getSprite().getX(), getSprite().getY(), 0);
            projector.project(coords);

            //displayActor.pack();

            // Show this table directly under entity, not over it
            getDescriptor().setPosition(coords.x, coords.y - getDescriptor().getHeight());

            stage.addActor(getDescriptor());
        }

        else
            getDescriptor().remove();
    }

    public boolean isDescriptorShown() {
        return isDescriptorShown;
    }

    public void setDescriptorShown(boolean descriptorShown) {
        isDescriptorShown = descriptorShown;
    }

    public Table getDescriptor() {
        return descriptor;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void damage(float damageAmount) {
        this.health -= damageAmount;
    }
}
