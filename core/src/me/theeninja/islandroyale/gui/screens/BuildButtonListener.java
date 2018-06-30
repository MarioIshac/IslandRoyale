package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class BuildButtonListener extends InputListener {
    private final BuildButton buildButton;

    BuildButtonListener(BuildButton buildButton) {
        this.buildButton = buildButton;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


        Vector2 relativeOverlayLocation = new Vector2(x, y);
        Vector2 absoluteOverlayLocation = getBuildButton().localToStageCoordinates(relativeOverlayLocation);

        getBuildButton().setBuildPosition(absoluteOverlayLocation);
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        getBuildButton().setBuildPosition(null);
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        // Indicates that building has been placed and dragging is irrelevant
        if (getBuildButton().getBuildPosition() == null)
            return;

        Vector2 relativeOverlayLocation = new Vector2(x, y);
        Vector2 absoluteOverlayLocation = getBuildButton().localToStageCoordinates(relativeOverlayLocation);

        getBuildButton().getBuildPosition().set(absoluteOverlayLocation);
    }

    public BuildButton getBuildButton() {
        return buildButton;
    }
}
