package com.heroslender.config.orm.bukkit;

import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapter;
import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapterFactory;
import com.heroslender.config.orm.common.ConfigurationLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.logging.Level;

public class BukkitConfigurationLoader<T> extends ConfigurationLoader<T, ConfigurationSection> {
    private static final BukkitTypeAdapterFactory TYPE_ADAPTER_FACTORY = BukkitTypeAdapterFactory.INSTANCE;

    protected BukkitConfigurationLoader(@NotNull ConfigurationSection config, @NotNull Runnable saveConfig, @NotNull Class<T> clazz) {
        super(config, saveConfig, clazz);
    }

    @Nullable
    public static <T> T load(@NotNull Plugin plugin, @NotNull Class<T> clazz) {
        Objects.requireNonNull(plugin, "plugin is null");

        return load(plugin, plugin.getConfig(), clazz);
    }

    @Nullable
    public static <T> T load(@NotNull Plugin plugin, @NotNull String section, @NotNull Class<T> clazz) {
        Objects.requireNonNull(plugin, "plugin is null");
        Objects.requireNonNull(section, "section is null");

        ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection(section);
        if (configurationSection == null) {
            plugin.getLogger().log(Level.WARNING, "The configuration section {0} does not exist, creating an empty one", section);
            configurationSection = plugin.getConfig().createSection(section);
        }

        return load(plugin, configurationSection, clazz);
    }

    @Nullable
    public static <T> T load(@NotNull Plugin plugin, @NotNull ConfigurationSection config, @NotNull Class<T> clazz) {
        Objects.requireNonNull(plugin, "plugin is null");

        return load(config, plugin::saveConfig, clazz);
    }

    @Nullable
    public static <T> T load(@NotNull ConfigurationSection config, @NotNull Runnable saveConfig, @NotNull Class<T> clazz) {
        Objects.requireNonNull(config, "config is null");
        Objects.requireNonNull(saveConfig, "saveConfig is null");
        Objects.requireNonNull(clazz, "clazz is null");

        final ConfigurationLoader<T, ConfigurationSection> loader = new BukkitConfigurationLoader<>(config, saveConfig, clazz);
        return loader.load();
    }

    @Override
    public Object getConfigValue(Field field, String valuePath, @Nullable Object defaultValue) {
        final BukkitTypeAdapter<?> typeAdapter = TYPE_ADAPTER_FACTORY.getTypeAdapter(field.getType());
        if (typeAdapter == null) {
            // No adapter was found, best shot, it's a sub-section X)
            if (!getConfig().isSet(valuePath)) {
                getLogger().log(Level.INFO, "The section {0} is not present in the config, creating it.", valuePath);
                getConfig().createSection(valuePath);
            }

            return new BukkitConfigurationLoader<>(
                    getConfig().getConfigurationSection(valuePath),
                    getSaveConfig(),
                    field.getType()
            ).load();
        }

        if (!getConfig().isSet(valuePath)) {
            getLogger().log(Level.INFO, "The field {0} is not present in the config, creating it.", valuePath);
            typeAdapter.saveDefault(getConfig(), valuePath, defaultValue);
            getSaveConfig().run();
        }

        return typeAdapter.get(getConfig(), valuePath, field);
    }
}