package com.heroslender.config.orm.common.adapter;

import org.apache.commons.lang.ClassUtils;

import java.util.List;

public interface TypeAdapterFactory<C extends TypeAdapter<?>> {
    List<C> getAdapterList();

    default <T> C getTypeAdapter(Class<T> type) {
        for (C adapter : getAdapterList()) {
            if (ClassUtils.isAssignable(type, adapter.getType(), true)) {
                return adapter;
            }
        }

        return null;
    }
}
