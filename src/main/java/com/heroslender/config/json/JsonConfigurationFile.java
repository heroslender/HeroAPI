package com.heroslender.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.heroslender.config.json.serializers.ItemStackDeserializer;
import com.heroslender.config.json.serializers.ItemStackSerializer;
import com.heroslender.config.json.serializers.LocationDeserializer;
import com.heroslender.config.json.serializers.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

class JsonConfigurationFile {
    final Gson gson;
    private final File configFile;
    private final JavaPlugin plugin;
    JsonObject config;

    JsonConfigurationFile(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(Location.class, new LocationDeserializer())
                .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
                .registerTypeAdapter(ItemStack.class, new ItemStackDeserializer())
                .create();

        if (!name.toLowerCase().endsWith(".json")) name += ".json";

        configFile = new File(plugin.getDataFolder(), name);

        try {
            loadFile();
            loadConfig();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Ocurreu um erro ao carregar a configuração!", e);
        }
    }

    public void reload() {
        try {
            loadFile();
            loadConfig();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Ocurreu um erro ao recarregar a configuração!", e);
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Ocurreu um erro ao guardar a configuração!", e);
        }
    }

    private void loadFile() throws IOException {
        File parent = configFile.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                log(Level.SEVERE, "Ocurreu um erro ao criar a configuração!");
                return;
            }
        }
        if (!configFile.exists()) {
            log(Level.WARNING, "A configuração \"" + configFile.getName() + "\" não foi encontrada, a criar uma nova...");

            try {
                // Save the config file if found inside the jar
                plugin.saveResource(configFile.getName(), false);
            } catch (IllegalArgumentException e) {
                // File not found in resources, creating a blank one
                if (configFile.createNewFile())
                    log("A configuração foi criada com sucesso!");
                else
                    log(Level.SEVERE, "Ocurreu um erro ao criar a configuração!");
            }
        }
    }

    private void loadConfig() throws Exception {
        log("Loading config");
        config = gson.fromJson(new JsonReader(new FileReader(configFile)), JsonObject.class);
        if (config == null)
            config = new Gson().fromJson("{}", JsonObject.class);
        log("Loaded " + (config != null));
    }

    private void log(String msg) {
        log(Level.INFO, msg);
    }

    private void log(Level logLevel, String msg) {
        plugin.getLogger().log(logLevel, "[JsonConfiguration] {0}", msg);
    }
}
