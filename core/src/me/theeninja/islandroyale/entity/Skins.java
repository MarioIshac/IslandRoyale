package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Skins implements Disposable {
    private static final Skins instance = new Skins();

    private Skins() {
        FileHandle flatEarthSkinFileHandler = Gdx.files.internal("flat-earth/skin/flat-earth-ui.json");
        this.flatEarthSkin = new Skin(flatEarthSkinFileHandler);
    }

    public static Skins getInstance() {
        return instance;
    }

    public final Skin flatEarthSkin;

    public Skin getFlatEarthSkin() {
        return flatEarthSkin;
    }

    @Override
    public void dispose() {
        getFlatEarthSkin().dispose();
    }
}
