package com.heroslender.config.orm;

import com.google.common.collect.Lists;
import com.heroslender.config.orm.bukkit.BukkitConfigurationLoader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        File configFile = new File("./config.yml");
        YamlConfiguration config = getTestYamlConfiguration(configFile);

        BukkitConfigurationLoader<Config> configurationLoader = new BukkitConfigurationLoader<>(
                config,
                () -> saveConfig(config, configFile),
                Config.class
        );

        Config cfg = configurationLoader.load();
        System.out.printf("Name before update: '%s'%n", cfg.name);

        cfg.name = "Unknown";
        configurationLoader.save(cfg);

        cfg = configurationLoader.load();
        System.out.printf("Name after update: '%s'%n", cfg.name);
    }

    private static YamlConfiguration getTestYamlConfiguration(final File configFile) {
        YamlConfiguration config = new YamlConfiguration();

        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();

            config.load(configFile);
        } catch (Exception e) {
            System.out.println("There was an error trying to load the sample test configuration: " + e.getMessage());
            e.printStackTrace();
        }

        return config;
    }

    private static void saveConfig(final YamlConfiguration config, final File configFile) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Config {
        // Sample string without value
        public String name;
        // Sample primitive without value
        public int age;

        // Sample list without value
        public List<String> emptyNames;
        // Sample list with default value
        public List<String> names = Lists.newArrayList("Fulano", "Beltrano", "Sicrano");

        // Sample sub-section
        public InnerConfig inner;

        // Sample map of strings and map of sub-sections
        // They're initialized in the constructor with default values
        Map<String, String> someMap = new HashMap<>();
        Map<String, InnerConfig> someMapOfObjects = new HashMap<>();

        // Constructor with no args is required!
        public Config() {
            someMap.put("Hello", "World");
            someMap.put("Hello2", "World2");

            someMapOfObjects.put("First", new InnerConfig("Bruno", 21));
            someMapOfObjects.put("Second", new InnerConfig("Heroslender", -1));
        }

        // toString method for testing
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
            // Sample string with default value in sub-section
            public String nameWithDefault = "Bruno";
            // Sample primitive with default value in sub-section
            public int ageWithDefault = 21;

            public InnerConfig() {
            }

            public InnerConfig(String nameWithDefault, int ageWithDefault) {
                this.nameWithDefault = nameWithDefault;
                this.ageWithDefault = ageWithDefault;
            }

            // toString method for testing
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
