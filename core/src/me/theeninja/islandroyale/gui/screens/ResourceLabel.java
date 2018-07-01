package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.Resource;
import me.theeninja.islandroyale.entity.Skins;

public class ResourceLabel extends HorizontalGroup {
    private final Resource resource;
    private final Player player;
    private final float DELAY_BETWEEN_UPDATE = 0.2f;

    private final Image image;
    private final Label label;

    private float secondsElapsed;

    public ResourceLabel(Resource resource, Texture texture, Player player) {
        super();

        this.resource = resource;
        this.player = player;

        this.image = new Image(texture);
        this.label = new Label(resource.name(), Skins.getInstance().getFlatEarthSkin());

        addActor(getImage());
        addActor(getLabel());
    }

    public Resource getResource() {
        return resource;
    }

    public void update(float delta) {
        secondsElapsed -= delta;

        float newResourceAmount = getPlayer().getInventory().get(getResource());
        String representation = String.valueOf(Math.round(newResourceAmount));

        getLabel().setText(resource.name() + " " + representation);
    }

    public Player getPlayer() {
        return player;
    }

    public Image getImage() {
        return image;
    }

    public Label getLabel() {
        return label;
    }
}
