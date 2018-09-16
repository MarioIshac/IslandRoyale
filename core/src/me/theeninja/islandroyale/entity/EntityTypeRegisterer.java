package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.IslandRoyaleGame;

public class EntityTypeRegisterer<A extends Entity<A, B>, B extends EntityType<A, B>> {
    private final EntityTypeManager entityTypeManager;
    private final Class<B> entityTypeClass;
    private final String directory;

    public EntityTypeRegisterer(EntityTypeManager entityTypeManager, Class<B> entityTypeClass, String directory) {
        this.entityTypeManager = entityTypeManager;
        this.entityTypeClass = entityTypeClass;
        this.directory = directory;
    }

    protected void registerEntityType(B entityType) {
        getEntityTypeManager().getEntityIDMap().put(entityType.getId(), entityType);
    }

    public final void registerEntityTypeDirectory() {
        FileHandle typesDescriptorFileHandle = Gdx.files.internal(directory + "types.txt");
        String text = typesDescriptorFileHandle.readString();

        System.out.println(directory + " is with " + getEntityTypeClass().getSimpleName());
        System.out.println("\"" + text + "\"");
        System.out.println();

        if (text.trim().isEmpty()) {
            return;
        }

        String[] typeFileNames = text.split("\\r?\\n");

        for (String typeFileName : typeFileNames) {
            FileHandle typeFileHandle = Gdx.files.internal(directory + typeFileName);
            B entityType = IslandRoyaleGame.JSON.fromJson(getEntityTypeClass(), typeFileHandle);

            String expandedTexturePath = directory + entityType.getTexturePath();
            FileHandle textureFileHandle = Gdx.files.internal(expandedTexturePath);
            Texture texture = new Texture(textureFileHandle);

            entityType.setTexture(texture);

            registerEntityType(entityType);
        }
    }

    public Class<B> getEntityTypeClass() {
        return entityTypeClass;
    }

    protected EntityTypeManager getEntityTypeManager() {
        return entityTypeManager;
    }
}
