package com.heroslender.config.orm;

import com.google.common.collect.Lists;
import com.heroslender.config.orm.bukkit.BukkitConfigurationLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException, InvalidConfigurationException {
        File configFile = new File("./config.yml");
        configFile.getParentFile().mkdirs();
        configFile.createNewFile();

        YamlConfiguration config = new YamlConfiguration();
        config.load(configFile);

        BukkitConfigurationLoader<Config> configurationLoader = new BukkitConfigurationLoader(config, () -> {
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Config.class);

        Config cfg = configurationLoader.load();
        System.out.printf("Name before update: '%s'%n", cfg.name);

        cfg.name = "Unknown";
        configurationLoader.save(cfg);

        cfg = configurationLoader.load();
        System.out.printf("Name after update: '%s'%n", cfg.name);
    }

    public static class Config {
        public String name;
        public int age;

        public List<String> emptyNames;
        public List<String> names = Lists.newArrayList("Fulano", "Beltrano", "Sicrano");

        public InnerConfig inner;

        Map<String, String> someMap = new HashMap<>();
        Map<String, InnerConfig> someMapOfObjects = new HashMap<>();

        public Config() {
            someMap.put("Hello", "World");
            someMap.put("Hello2", "World2");

            someMapOfObjects.put("First", new InnerConfig("Bruno", 21));
            someMapOfObjects.put("Second", new InnerConfig("Heroslender", -1));
        }

        @Override
        public String toString() {
            return "Config{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", emptyNames=" + emptyNames +
                    ", names=" + names +
                    ", \ninner=" + inner +
                    ", \nsomeMap=" + someMap +
                    ", \nsomeMapOfObjects=" + someMapOfObjects +
                    "\n}";
        }

        public static class InnerConfig {
            public String nameWithDefault = "Bruno";
            public int ageWithDefault = 21;

            public InnerConfig() {
            }

            public InnerConfig(String nameWithDefault, int ageWithDefault) {
                this.nameWithDefault = nameWithDefault;
                this.ageWithDefault = ageWithDefault;
            }

            @Override
            public String toString() {
                return "InnerConfig{" +
                        "nameWithDefault='" + nameWithDefault + '\'' +
                        ", ageWithDefault=" + ageWithDefault +
                        '}';
            }
        }
    }
}
