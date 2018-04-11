package me.theeninja.islandroyale.building;

public abstract class DefenseBuilding extends Building {

    /**
     * @return a number representing the base damage one attack does
     */
    public abstract float getBaseDamage();

    /**
     * @return how many times the defensive building will attack per second, at level 1
     */
    public abstract float getBaseFireRate();

    /**
     * @return a number between 0 and 1, representing the base chance of critical damage being applied
     */
    public abstract float getBaseCriticalChance();

    /**
     * @return the average amount of base damage this building does per second
     */
    public float getBaseDamagePerSecond() {
        return getBaseDamage() * getBaseFireRate();
    }
}
