package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.IslandRoyaleGame;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeFactory<T extends EntityType<T>> {
    private final Class<T> classType;
    private final String directory;
    private final List<T> entityTypes = new ArrayList<>();

    public EntityTypeFactory(String directory, Class<T> classType) {
        this.classType = classType;
        this.directory = directory;

        loadEntityTypes();
    }
    public Class<T> getClassType() {
        return classType;
    }

    public String getDirectory() {
        return directory;
    }

    public void loadEntityTypes() {
        FileHandle typesDescriptorFileHandle = Gdx.files.internal(directory + "types.txt");
        String text = typesDescriptorFileHandle.readString();

        if (!text.isEmpty()) {
            String[] typeFileNames = text.split("\\r?\\n");

            for (String typeFileName : typeFileNames) {
                FileHandle typeFileHandle = Gdx.files.internal(directory + typeFileName);
                T type = IslandRoyaleGame.JSON.fromJson(getClassType(), typeFileHandle);

                EntityType.IDS.put(type.getId(), type);

                String expandedTexturePath = directory + type.getTexturePath();
                FileHandle textureFileHandle = Gdx.files.internal(expandedTexturePath);
                Texture texture = new Texture(textureFileHandle);

                type.setTexture(texture);

                getEntityTypes().add(type);
            }
        }
    }

    public List<T> getEntityTypes() {
        return entityTypes;
    }
}
