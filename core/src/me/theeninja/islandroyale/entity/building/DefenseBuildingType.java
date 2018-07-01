package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DefenseBuildingType extends BuildingType<DefenseBuilding, DefenseBuildingType> {

    private Sprite rangeSprite;

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

    public Sprite getRangeSprite() {
        return rangeSprite;
    }

    @Override
    public void setUpEntityType(DefenseBuildingType entityType) {
        super.setUpEntityType(entityType);

        FileHandle rangeFileHandle = Gdx.files.internal("Range.png");
        Texture rangeTexture = new Texture(rangeFileHandle);
        this.rangeSprite = new Sprite(rangeTexture);

        getRangeSprite().setSize(1, 1);
    }
}
