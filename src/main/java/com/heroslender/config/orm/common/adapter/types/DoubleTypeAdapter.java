package com.heroslender.config.orm.common.adapter.types;

import com.heroslender.config.orm.common.adapter.TypeAdapter;

public class DoubleTypeAdapter implements TypeAdapter<Double> {
    @Override
    public Class<Double> getType() {
        return Double.TYPE;
    }

    @Override
    public Double from(String value) {
        return Double.parseDouble(value);
    }
}
