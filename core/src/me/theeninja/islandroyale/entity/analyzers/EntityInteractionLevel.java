package me.theeninja.islandroyale.entity.analyzers;

import com.badlogic.gdx.graphics.Color;

import java.util.function.Predicate;

public enum EntityInteractionLevel {
    MINOR(Color.PINK),
    MAJOR(Color.ORANGE),
    DEATH(Color.RED);

    private Color color;

    EntityInteractionLevel(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
