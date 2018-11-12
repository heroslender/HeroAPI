package com.heroslender.config.json.serializers;

import com.google.gson.*;
import com.heroslender.config.json.exceptions.InvalidLocationException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationDeserializer implements JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObj = json.getAsJsonObject();

        // Pegar as bases da location da config
        JsonElement worldElement = jObj.get("world");
        JsonElement xElement = jObj.get("x");
        JsonElement yElement = jObj.get("y");
        JsonElement zElement = jObj.get("z");

        // Verificar se as mesmas bases sao validas
        if (worldElement == null || xElement == null || yElement == null || zElement == null ||
                !worldElement.isJsonPrimitive() || !xElement.isJsonPrimitive() || !yElement.isJsonPrimitive() || !zElement.isJsonPrimitive())
            throw new InvalidLocationException("Localização invalida!");

        // Pegar o Yaw e o Pitch que sao opcionais
        // Caso eles nao se encontrem na config, o valor sera 0(zero)
        JsonElement yawElement = jObj.get("yaw");
        JsonElement pitchElement = jObj.get("pitch");

        Float yaw = 0.0F;
        Float pitch = 0.0F;

        if (yawElement != null && yawElement.isJsonPrimitive())
            yaw = yawElement.getAsFloat();
        if (pitchElement != null && pitchElement.isJsonPrimitive())
            pitch = pitchElement.getAsFloat();

        // Ultimo passo, pegar a location apartir dos valores :D
        // Aqui pode dar uma Parse Exception se o utilizador tretar na config
        return new Location(getWorld(worldElement.getAsString()),
                xElement.getAsDouble(),
                yElement.getAsDouble(),
                zElement.getAsDouble(),
                yaw,
                pitch
        );
    }

    private World getWorld(String nome) throws InvalidLocationException {
        World world = Bukkit.getWorld(nome);
        if (world == null) throw new InvalidLocationException("O mundo '" + nome + "' não existe no servidor!");
        return world;
    }
}
