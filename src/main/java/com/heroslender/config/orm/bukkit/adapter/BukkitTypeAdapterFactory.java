package com.heroslender.config.orm.bukkit.adapter;

import com.google.common.collect.Lists;
import com.heroslender.config.orm.bukkit.adapter.types.ListTypeAdapter;
import com.heroslender.config.orm.bukkit.adapter.types.PrimitiveTypeAdapters;
import com.heroslender.config.orm.common.adapter.TypeAdapterFactory;

import java.util.List;

public class BukkitTypeAdapterFactory implements TypeAdapterFactory<BukkitTypeAdapter<?>> {
    public static final BukkitTypeAdapterFactory INSTANCE = new BukkitTypeAdapterFactory();
    private static List<BukkitTypeAdapter<?>> adapterList;

    private BukkitTypeAdapterFactory() {
    }

    @Override
    public synchronized List<BukkitTypeAdapter<?>> getAdapterList() {
        if (adapterList == null) {
            adapterList = Lists.newArrayList();
            adapterList.addAll(PrimitiveTypeAdapters.PRIMITIVE_ADAPTERS);
            adapterList.add(ListTypeAdapter.INSTANCE);
        }

        return adapterList;
    }
}
