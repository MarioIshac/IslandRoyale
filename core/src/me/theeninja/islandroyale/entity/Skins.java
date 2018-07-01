package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Skins {
    private static final Skins instance = new Skins();

    private Skins() {
        FileHandle flatEarthSkinFileHandler = new FileHandle("flat-earth/skin/flat-earth-ui.json");
        this.flatEarthSkin = new Skin(flatEarthSkinFileHandler);
    }

    public static Skins getInstance() {
        return instance;
    }

    public final Skin flatEarthSkin;

    public Skin getFlatEarthSkin() {
        return flatEarthSkin;
    }
}
