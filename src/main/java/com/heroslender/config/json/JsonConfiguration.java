package com.heroslender.config.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class JsonConfiguration extends JsonConfigurationFile {

    public JsonConfiguration(JavaPlugin plugin) {
        this(plugin, "config");
    }

    public JsonConfiguration(JavaPlugin plugin, String configName) {
        super(plugin, configName);
    }

    public Integer getInteger(String path) {
        return getInteger(path, null);
    }

    public Integer getInteger(String path, Integer defaultValue) {
        return get(path, defaultValue, Integer.class);
    }

    public Double getDouble(String path) {
        return getDouble(path, null);
    }

    public Double getDouble(String path, Double defaultValue) {
        return get(path, defaultValue, Double.class);
    }

    public String getString(String path) {
        return getString(path, null);
    }

    public String getString(String path, String defaultValue) {
        return get(path, defaultValue, String.class);
    }

    public Long getLong(String path) {
        return getLong(path, null);
    }

    public Long getLong(String path, Long defaultValue) {
        return get(path, defaultValue, Long.class);
    }

    public List<String> getStringList(String path) {
        return getStringList(path, null);
    }

    public List<String> getStringList(String path, List<String> defaultValue) {
        return get(path, defaultValue, List.class);
    }

    public <T> List<T> getList(String path) {
        return getList(path, null);
    }

    public <T> List<T> getList(String path, List<T> defValue) {
        return get(path, defValue, List.class);
    }

    public Location getLocation(String path) {
        return getLocation(path, null);
    }

    public Location getLocation(String path, Location defaultValue) {
        return get(path, defaultValue, Location.class);
    }

    public ItemStack getItemStack(String path) {
        return getItemStack(path, null);
    }

    public ItemStack getItemStack(String path, ItemStack defaultValue) {
        return get(path, defaultValue, ItemStack.class);
    }

    public Collection<String> getKeys(String path) {
        JsonElement jElement = get(path);
        if (jElement != null && jElement.isJsonObject()) {
            JsonObject jObject = jElement.getAsJsonObject();
            return jObject.keySet();
        }
        return Collections.emptyList();
    }

    private <T> T get(String path, T defaultValue, Type type) {
        try {
            T valor = gson.fromJson(get(path), type);
            return valor == null ? defaultValue : valor;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public void addDefault(String path, Object value) {
        if (!contains(path)) set(path, value);
    }

    public void set(String path, Object value) {
        if (!path.contains(".")) {
            set(config, path, value);
            return;
        }

        String[] subPaths = path.split("\\.");
        JsonObject prev = config;
        for (int i = 0; i < subPaths.length; i++) {
            String sectionName = subPaths[i];
            if (i == subPaths.length - 1) {
                set(prev, sectionName, value);
                return;
            }

            if (!prev.has(sectionName)) {
                JsonObject section = new JsonObject();
                prev.add(sectionName, section);
                prev = section;
                continue;
            }

            prev = prev.getAsJsonObject(sectionName);
        }
    }

    private boolean set(JsonObject jsonObject, String path, Object value) {
        jsonObject.add(path, gson.toJsonTree(value));
        save();
        return true;
    }

    public boolean contains(String path) {
        try {
            return get(path) != null;
        } catch (Exception e) {
            return false;
        }
    }

    private JsonElement get(String path) {
        if (!path.contains(".")) return config.get(path);

        StringTokenizer tokenizer = new StringTokenizer(path, ".");
        JsonElement prev = config;
        while (tokenizer.hasMoreTokens()) {
            if (!(prev instanceof JsonObject)) return null;

            JsonObject prevObj = prev.getAsJsonObject();
            String subPath = tokenizer.nextToken();
            if (!prevObj.has(subPath)) return null;

            prev = prevObj.get(subPath);
        }

        return prev;
    }
}
