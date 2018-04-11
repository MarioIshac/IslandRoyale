package me.theeninja.islandroyale.building;

import me.theeninja.islandroyale.Resource;

import java.util.HashMap;
import java.util.Map;

public abstract class ResourceBuilding extends Building {
    public static final String DIRECTORY = "resource/";

    /**
     * Represents the base production rates of every contained resource. The value corresponding to
     * each key represents how much of said resource is to be generated per second at level 1.
     */
    private final Map<Resource, Float> baseProductionRates = new HashMap<>();

    public Map<Resource, Float> getBaseProductionRates() {
        return baseProductionRates;
    }
}
