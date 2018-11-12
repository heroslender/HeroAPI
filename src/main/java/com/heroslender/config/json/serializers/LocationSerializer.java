package com.heroslender.config.json.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject locJson = new JsonObject();

        locJson.addProperty("world", src.getWorld().getName());
        locJson.addProperty("x", src.getX() + "");
        locJson.addProperty("y", src.getY() + "");
        locJson.addProperty("z", src.getZ() + "");
        if (src.getYaw() != 0.0)
            locJson.addProperty("yaw", src.getYaw() + "");
        if (src.getPitch() != 0.0)
            locJson.addProperty("pitch", src.getPitch() + "");

        return locJson;
    }
}
