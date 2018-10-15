package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class InteractableEntity<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends Entity<A, B> {
    public InteractableEntity(B entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y);

        getEntityType().configureEditor(getReference(), match);
        //setHealth(getEntityType().getBaseHealth());
        //setLevel(getEntityType().getBaseLevel(getReference()));
    }

    private static final String BASE_HEALTH_FIELD_NAME = "baseHealth";

    @EntityAttribute(BASE_HEALTH_FIELD_NAME)
    private float health;

    private final Table descriptor = new Table();
    private boolean descriptorShown;
    private boolean hasUserRemoved;

    private UpgradeListener<A, B> upgradeListener;
    private Button upgradeButton;

    private static final String UPGRADE_TEXT = "Upgrade";

    private void updateUpgradable(boolean isUpgradable) {
        getUpgradeListener().setUpgradable(isUpgradable);
        getUpgradeButton().setDisabled(true);
    }

    @Override
    public void initializeConstructorDependencies() {
        this.upgradeListener = new UpgradeListener<>(getReference());
        this.upgradeButton = new TextButton(UPGRADE_TEXT, Skins.getInstance().getFlatEarthSkin());
    }

    @Override
    public boolean shouldRemove() {
        return getHealth() <= 0 || hasUserRemoved();
    }

    @Override
    public void check(float delta, Player player, Match match) {
        // If entity is removed, hide descriptor as entity is hidden as well
        if (shouldRemove())
            this.descriptorShown = false;

        updateUpgradable(calculateUpgradable());
    }

    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {
        if (isDescriptorShown()) {
            getDescriptor().pack();

            float topLeftX = getX();
            float topLeftY = getY() + getHeight();

            Vector3 entityHUDCoordinates = new Vector3(topLeftX, topLeftY, 0);

            // Entity Coordinates in Pixels On Screen
            projector.project(entityHUDCoordinates);
            //hudStage.getCamera().unproject(entityHUDCoordinates);

            getDescriptor().setPosition(entityHUDCoordinates.x, entityHUDCoordinates.y);

            hudStage.addActor(getDescriptor());
        }

        else
            getDescriptor().remove();
    }

    public boolean isDescriptorShown() {
        return descriptorShown;
    }

    public void setDescriptorShown(boolean descriptorShown) {
        System.out.println(("Setting desriptor shown to " + descriptorShown));
        this.descriptorShown = descriptorShown;
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

    public boolean hasUserRemoved() {
        return hasUserRemoved;
    }

    public void setHasUserRemoved(boolean hasUserRemoved) {
        this.hasUserRemoved = hasUserRemoved;
    }

    public UpgradeListener<A, B> getUpgradeListener() {
        return upgradeListener;
    }

    protected abstract boolean calculateUpgradable();

    public Button getUpgradeButton() {
        return upgradeButton;
    }
}
