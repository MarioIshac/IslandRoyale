package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.IslandRoyaleGame;

public class EntityTypeFactory<T extends EntityType<T>> {
    private final Class<T> classType;
    private final String directory;
    private final Array<T> entityTypes = new Array<>();

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
        String expandedDirectory = EntityType.ENTITY_DIRECTORY + directory;
        FileHandle typesDescriptorFileHandle = Gdx.files.internal(expandedDirectory + "types.txt");
        String text = typesDescriptorFileHandle.readString();

        if (!text.isEmpty()) {
            String[] typeFileNames = text.split("\\r?\\n");

            for (String typeFileName : typeFileNames) {
                FileHandle typeFileHandle = Gdx.files.internal(expandedDirectory + typeFileName);
                T type = IslandRoyaleGame.JSON.fromJson(getClassType(), typeFileHandle);

                String expandedTexturePath = expandedDirectory + type.getTexturePath();
                FileHandle textureFileHandle = Gdx.files.internal(expandedTexturePath);
                Texture texture = new Texture(textureFileHandle);

                type.setTexture(texture);

                getEntityTypes().add(type);
            }
        }
    }

    public Array<T> getEntityTypes() {
        return entityTypes;
    }
}
