package com.heroslender.config.orm.bukkit.adapter;

import com.heroslender.config.orm.common.adapter.TypeAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public interface BukkitTypeAdapter<T> extends TypeAdapter<T> {

    T get(ConfigurationSection configuration, String path);

    default T get(ConfigurationSection configurationSection, String path, Field field) {
        return get(configurationSection, path);
    }

    void saveDefault(ConfigurationSection configuration, String path, Object defaultValue);

    default void saveDefault(ConfigurationSection configuration, String path, Object defaultValue, Field field) {
        saveDefault(configuration, path, defaultValue);
    }
}
