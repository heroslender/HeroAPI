package com.heroslender.config.orm.common.adapter;

public interface TypeAdapter<T> {
    Class<T> getType();

    T from(String value);
}