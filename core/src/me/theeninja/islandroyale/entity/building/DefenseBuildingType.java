package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import me.theeninja.islandroyale.entity.Attacker;
import me.theeninja.islandroyale.entity.AttackerType;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectileType;

public final class DefenseBuildingType extends BuildingType<DefenseBuilding, DefenseBuildingType> implements AttackerType<DefenseBulletProjectile, DefenseBulletProjectileType, DefenseBuilding, DefenseBuildingType> {
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

    public float getBaseRange() {
        return baseRange;
    }

    @Override
    public int getEntityTypeIndex() {
        return DEFENSE_BUILDING_ENTITY_TYPE;
    }
}
