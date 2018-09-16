package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;
import me.theeninja.islandroyale.entity.controllable.PersonType;
import me.theeninja.islandroyale.entity.controllable.TransporterType;
import me.theeninja.islandroyale.gui.screens.Match;

import java.util.List;

/**
 * @param <A> the <b>t</b>ype of the class that is subclassing OffensiveBuildingType
 * @param <B> the entity type that this offense building <b>p</b>roduces (most likely between
 *           {@link PersonType} and
 *           {@link TransporterType}
 */
public abstract class OffenseBuildingType<A extends OffenseBuilding<A, B, C, D>, B extends OffenseBuildingType<A, B, C, D>, C extends ControllableEntity<C, D>, D extends ControllableEntityType<C, D>> extends BuildingType<A, B> {
    private List<Integer> entityIDsProduced;

    @Override
    public void configureEditor(A entity, Match match) {
        super.configureEditor(entity, match);

        Label queueLabel = new Label("Queue", Skins.getInstance().getFlatEarthSkin());
        entity.getDescriptor().add(queueLabel).row();

        for (int entityID : getEntityIDsProduced()) {
            D entityTypeProduced = match.getEntityTypeManager().getEntityType(entityID);

            Actor queueButton = new TextButton(entityTypeProduced.getName(), Skins.getInstance().getFlatEarthSkin());

            QueueButtonListener<C, D> queueButtonListener = new QueueButtonListener<>(entityTypeProduced);
            queueButton.addListener(queueButtonListener);

            entity.getDescriptor().add(queueButton).row();
            entity.getQueueButtonListeners().add(queueButtonListener);
        }

        entity.getDescriptor().add(entity.getQueueDisplay()).row();
    }

    public List<Integer> getEntityIDsProduced() {
        return entityIDsProduced;
    }
}
