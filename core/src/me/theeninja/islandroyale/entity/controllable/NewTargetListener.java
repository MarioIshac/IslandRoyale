package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.EntityListener;

public class NewTargetListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends EntityListener<A, B> {
    NewTargetListener(A entity) {
        super(entity);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("New Target Requested");

        getEntity().getPathSelectionInputListener().request();

        // Selecting the path required the descriptor of the entity to be removed from the stage
        getEntity().setDescriptorShown(false);

        return true;
    }
}
