package com.heroslender.config.orm.bukkit.adapter.types;

import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapter;
import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapterFactory;
import com.heroslender.config.orm.common.adapter.TypeAdapter;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ListTypeAdapter implements BukkitTypeAdapter<List> {
    public static final ListTypeAdapter INSTANCE = new ListTypeAdapter();
    private static final Class<List> CLAZZ = List.class;

    private ListTypeAdapter() {
    }

    @Override
    public Class<List> getType() {
        return CLAZZ;
    }

    @Override
    public List from(String value) {
        return Collections.singletonList(value);
    }

    @Override
    public List get(ConfigurationSection configuration, String path) {
        System.out.println("a");
        return configuration.getStringList(path);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List get(ConfigurationSection configurationSection, String path, Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> type = (Class<?>) stringListType.getActualTypeArguments()[0];

        TypeAdapter<?> adapter = BukkitTypeAdapterFactory.INSTANCE.getTypeAdapter(type);
        if (adapter == null) {
            System.out.println("No adapter found for " + type.getSimpleName());
            return Collections.emptyList();
        }

        List list = new ArrayList();
        for (String s : configurationSection.getStringList(path)) {
            list.add(adapter.from(s));
        }
        return list;
    }



    @Override
    public void saveDefault(ConfigurationSection configuration, String path, Object defaultValue) {
        if (defaultValue == null) {
            defaultValue = Collections.emptyList();
        }

        configuration.set(path, defaultValue);
    }
}
