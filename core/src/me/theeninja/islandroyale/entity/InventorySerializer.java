package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import me.theeninja.islandroyale.Inventory;
import me.theeninja.islandroyale.Resource;

import java.util.HashMap;
import java.util.Map;

public class InventorySerializer implements Json.Serializer<Inventory> {
    private static final String RESOURCE_ENTRIES_LABEL = "resourceEntries";
    private static final String RESOURCE_LABEL = "resource";
    private static final String COST_LABEL = "cost";

    @Override
    public void write(Json json, Inventory inventory, Class knownType) {
        json.writeObjectStart();
        json.writeArrayStart(RESOURCE_ENTRIES_LABEL);

        inventory.getResourceMap().forEach((resource, cost) -> {
            json.writeObjectStart();
            json.writeValue(RESOURCE_LABEL, resource, Resource.class);
            json.writeValue(COST_LABEL, cost, Float.class);
            json.writeObjectEnd();
        });

        json.writeArrayEnd();
        json.writeArrayEnd();
    }

    @Override
    public Inventory read(Json json, JsonValue jsonData, Class type) {
        Inventory inventory = new Inventory();

        JsonValue entries = jsonData.child;

        for (JsonValue child = entries.child; child != null; child = child.next) {
            JsonValue resourceValue = child.get(RESOURCE_LABEL);
            Resource resource = json.readValue(Resource.class, resourceValue);

            JsonValue costValue = child.get(COST_LABEL);
            float cost = json.readValue(Float.class, costValue);

            inventory.getResourceMap().put(resource, cost);
        }

        return inventory;
    }
}
