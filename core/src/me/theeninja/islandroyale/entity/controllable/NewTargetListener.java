package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public class NewTargetListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InputListener {
    private final A entity;

    NewTargetListener(A entity) {
        this.entity = entity;
    }

    private PathSelectionInputListener pathSelectionInputListener;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (getPathSelectionInputListener() != null)
            getEntity().getStage().removeListener(getPathSelectionInputListener());

        setPathSelectionInputListener(new PathSelectionInputListener(getEntity()));
        getEntity().getStage().addListener(getPathSelectionInputListener());
        return true;
    }



    public A getEntity() {
        return entity;
    }

    public PathSelectionInputListener getPathSelectionInputListener() {
        return pathSelectionInputListener;
    }

    public void setPathSelectionInputListener(PathSelectionInputListener pathSelectionInputListener) {
        this.pathSelectionInputListener = pathSelectionInputListener;
    }
}
