package com.heroslender.config.orm.common.adapter.exceptions;

@SuppressWarnings("unused")
public class AdapterNotFoundException extends RuntimeException {
    private final Class<?> clazz;

    public AdapterNotFoundException(Class<?> clazz) {
        this("No adapter found for the class " + clazz.getName(), clazz);
    }

    public AdapterNotFoundException(String message, Class<?> clazz) {
        super(message);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
