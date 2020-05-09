package com.heroslender.config.orm.bukkit.adapter.types;//package com.heroslender.config.orm.bukkit.adapter.types;
//
//import com.heroslender.config.orm.bukkit.adapter.BukkitTypeAdapter;
//import org.bukkit.configuration.ConfigurationSection;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class StringListTypeAdapter implements BukkitTypeAdapter<List<String>> {
//    public static final StringListTypeAdapter INSTANCE = new StringListTypeAdapter();
//    private static final Class<List<String>> CLAZZ;
//
//    static {
//        // Hack to get the Class<List<String>>
//        // Source: https://stackoverflow.com/a/22707123
//        //noinspection InstantiatingObjectToGetClassObject
//        Class<?> arrayListClass = new ArrayList<String>().getClass();
//        Class<?>[] interfaces = arrayListClass.getInterfaces();
//        int index = 0;
//        for (int i = 0; i < interfaces.length; i++) {
//            if (interfaces[i].equals(List.class)) {
//                index = i;
//                break;
//            }
//        }
//        //noinspection unchecked
//        CLAZZ = (Class<List<String>>) interfaces[index];
//    }
//
//    private StringListTypeAdapter() {
//    }
//
//    @Override
//    public Class<List<String>> getType() {
//        return CLAZZ;
//    }
//
//    @Override
//    public List<String> get(ConfigurationSection configuration, String path) {
//        return configuration.getStringList(path);
//    }
//
//    @Override
//    public void saveDefault(ConfigurationSection configuration, String path, Object defaultValue) {
//        if (defaultValue == null) {
//            defaultValue = Collections.emptyList();
//        }
//
//        configuration.set(path, defaultValue);
//    }
//}
