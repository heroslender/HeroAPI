package com.heroslender.config.orm.common.adapter.types;

import com.heroslender.config.orm.common.adapter.TypeAdapter;

public class FloatTypeAdapter implements TypeAdapter<Float> {
    @Override
    public Class<Float> getType() {
        return Float.TYPE;
    }

    @Override
    public Float from(String value) {
        return Float.parseFloat(value);
    }
}
