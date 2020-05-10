package com.heroslender.config.orm.bukkit.adapter.types;

import com.heroslender.config.orm.bukkit.BukkitConfigurationLoader;
import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.Collections;

@SuppressWarnings("rawtypes")
public class ObjectTypeAdapter implements BukkitTypeAdapter<Object> {
    public static final ObjectTypeAdapter INSTANCE = new ObjectTypeAdapter();
    private static final Class<Object> CLAZZ = Object.class;

    private ObjectTypeAdapter() {
    }

    @Override
    public Class<Object> getType() {
        return CLAZZ;
    }

    @Override
    public Object from(String value) {
        return Collections.singletonList(value);
    }

    @Override
    public Object get(ConfigurationSection configurationSection, String path, Field field) {
        return BukkitConfigurationLoader.load(
                configurationSection.getConfigurationSection(path),
                null,
                field.getType()
        );
    }

    @Override
    public void saveDefault(ConfigurationSection configuration, String path, Object defaultValue, Field field) {
        configuration.createSection(path);

        if (defaultValue == null) {
            try {
                defaultValue = BukkitConfigurationLoader.load(
                        configuration.getConfigurationSection(path),
                        null,
                        field.getType()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        configuration.set(path, defaultValue);
    }

    @Override
    public Object get(ConfigurationSection configuration, String path) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void saveDefault(ConfigurationSection configuration, String path, Object defaultValue) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
