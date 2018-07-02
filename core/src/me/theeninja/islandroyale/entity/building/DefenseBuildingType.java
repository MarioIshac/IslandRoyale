package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DefenseBuildingType extends BuildingType<DefenseBuilding, DefenseBuildingType> {
    private float baseDamage;

    /**
     * Represents the shots per second to be shot from the defense building. Note that if this is greater
     * than the frames per second of the application, the fire rate will functionally be lowered
     * to the frames per second itself due to how this is handled.
     */
    private float baseFireRate;

    private float baseRange;

    /**
     * Represents the ID of the static bullet that this defense building fires (such as a cannon ball,
     * arrow, bullet, etc).
     */
    private int staticProjectileID;


    public float getBaseFireRate() {
        return baseFireRate;
    }

    public int getStaticProjectileID() {
        return staticProjectileID;
    }

    public float getBaseDamage() {
        return baseDamage;
    }
}
