package com.heroslender.config.orm.common.adapter;

import org.apache.commons.lang.ClassUtils;

import java.util.List;

public interface TypeAdapterFactory<C extends TypeAdapter<?>> {
    List<C> getAdapterList();

    default <T> C getTypeAdapter(Class<T> type) {
        for (C adapter : getAdapterList()) {
            System.out.println(adapter.getType().getSimpleName() + " - " + type.getSimpleName() + " - " + ClassUtils.isAssignable(adapter.getType(), type, true));

            if (ClassUtils.isAssignable(adapter.getType(), type, true)) {
                return adapter;
            }
        }

        return null;
    }
}
