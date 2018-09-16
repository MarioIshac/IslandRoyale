package me.theeninja.islandroyale;

public enum MapMode {
    VERY_SPARE(9, 0.85f),
    SPARSE(9, 0.8f),
    NEUTRAL(9, 0.75f),
    DENSE(9, 0.7f),
    VERY_DENSE(9, 0.25f);

    private final int octaveCount;
    private final float minimumIslandHeightPercentage;
    private final float maximumGrassHeightPercentage;

    MapMode(int octaveCount, float minimumIslandHeightPercentage) {
        this.octaveCount = octaveCount;
        this.minimumIslandHeightPercentage = minimumIslandHeightPercentage;
        this.maximumGrassHeightPercentage = 1.05f * getMinimumIslandHeightPercentage();
    }

    public int getOctaveCount() {
        return octaveCount;
    }

    public float getMinimumIslandHeightPercentage() {
        return minimumIslandHeightPercentage;
    }

    public float getMaximumGrassHeightPercentage() {
        return maximumGrassHeightPercentage;
    }
}
