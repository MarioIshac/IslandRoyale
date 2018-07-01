package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.IslandRoyaleGame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityTypeFactory<A extends Entity<A, B>, B extends EntityType<A, B>> {
    private final Class<B> classType;
    private final String directory;
    private final Array<B> entityTypes = new Array<B>();

    public EntityTypeFactory(String directory, Class<B> classType) {
        this.classType = classType;
        this.directory = directory;
    }
    public Class<B> getClassType() {
        return classType;
    }

    public String getDirectory() {
        return directory;
    }

    public void loadEntityTypes(Consumer<B> entityTypeConsumer) {
        FileHandle typesDescriptorFileHandle = Gdx.files.internal(directory + "types.txt");
        String text = typesDescriptorFileHandle.readString();

        if (!text.isEmpty()) {
            String[] typeFileNames = text.split("\\r?\\n");

            for (String typeFileName : typeFileNames) {
                FileHandle typeFileHandle = Gdx.files.internal(directory + typeFileName);
                B entityType = IslandRoyaleGame.JSON.fromJson(getClassType(), typeFileHandle);

                String expandedTexturePath = getDirectory() + entityType.getTexturePath();
                FileHandle textureFileHandle = Gdx.files.internal(expandedTexturePath);
                Texture texture = new Texture(textureFileHandle);

                entityType.setTexture(texture);

                entityTypeConsumer.accept(entityType);
            }
        }
    }

    public Array<B> getEntityTypes() {
        return entityTypes;
    }
}
