package me.theeninja.islandroyale.entity;

import me.theeninja.islandroyale.ai.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflexiveEntityResponse {
    private final Constructor<? extends InteractableEntity> interactableEntityConstructor;
    private final InteractableEntityType<?, ?> interactableEntityType;

    ReflexiveEntityResponse(Constructor<? extends InteractableEntity> interactableEntityConstructor, InteractableEntityType<?, ?> interactableEntityType) {
        this.interactableEntityConstructor = interactableEntityConstructor;
        this.interactableEntityType = interactableEntityType;
    }

    public InteractableEntity<?, ?> get(float x, float y, Player player) {
        try {
            return getInteractableEntityConstructor().newInstance(getInteractableEntityType(), x, y, player);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<? extends InteractableEntity> getInteractableEntityConstructor() {
        return interactableEntityConstructor;
    }

    private InteractableEntityType<?, ?> getInteractableEntityType() {
        return interactableEntityType;
    }
}
