package com.heroslender.config.orm.common.adapter.types;

import com.heroslender.config.orm.common.adapter.TypeAdapter;

public class StringTypeAdapter implements TypeAdapter<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String from(String value) {
        return value;
    }
}
