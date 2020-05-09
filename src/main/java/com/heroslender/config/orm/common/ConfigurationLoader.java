package com.heroslender.config.orm.common;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ConfigurationLoader<T, C> {
    @Getter private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    @Getter private final C config;
    @Getter private final Runnable saveConfig;
    @Getter private final Class<T> clazz;

    protected ConfigurationLoader(@NotNull C config, @NotNull Runnable saveConfig, @NotNull Class<T> clazz) {
        this.config = config;
        this.saveConfig = saveConfig;
        this.clazz = clazz;
    }

    public T load() {
        final T instance = initClass(clazz);
        if (instance == null) {
            return null;
        }

        for (Field field : clazz.getDeclaredFields()) {
            try {
                loadField(field, instance);
            } catch (IllegalAccessException e) {
                logger.log(Level.SEVERE, e, () -> "Failed to initialize the field " + field.getName());
            }
        }

        return instance;
    }

    protected void loadField(@NotNull final Field field, @NotNull final Object instance) throws IllegalAccessException {
        field.setAccessible(true);

        final String configFieldName = field.getName();

        final Object configValue = getConfigValue(field, configFieldName, field.get(instance));
        if (configValue != null) {
            field.set(instance, configValue);
        }
    }

    public abstract Object getConfigValue(Field field, String valuePath, @Nullable Object defaultValue);

    @Nullable
    protected T initClass(@NotNull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");

        T instance = null;
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            instance = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            getLogger().log(Level.SEVERE, e, () -> "Couln't find the default constructor for the class " + clazz.getSimpleName());
        } catch (IllegalAccessException | InstantiationException e) {
            getLogger().log(Level.SEVERE, e, () -> "Failed to initialize the class " + clazz.getSimpleName());
        } catch (InvocationTargetException e) {
            getLogger().log(Level.SEVERE, e.getCause(), () -> "An exception was thrown when trying to initialize the class " + clazz.getSimpleName());
        }

        return instance;
    }
}
