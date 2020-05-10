package com.heroslender.config.orm;

import com.google.common.collect.Lists;
import com.heroslender.config.orm.bukkit.BukkitConfigurationLoader;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, InvalidConfigurationException {
        File configFile = new File("./config.yml");
        configFile.getParentFile().mkdirs();
        configFile.createNewFile();

        YamlConfiguration config = new YamlConfiguration();
        config.load(configFile);

        Config cfg = BukkitConfigurationLoader.load(config, () -> {
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Config.class);

        System.out.println(cfg.toString());
    }

    public static class Config {
        public String name;
        public int age;

        public List<String> emptyNames;
        public List<String> names = Lists.newArrayList("Fulano", "Beltrano", "Sicrano");

        public InnerConfig inner;

        public Config() {
        }

        @Override
        public String toString() {
            return "Config{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", emptyNames=" + emptyNames +
                    ", names=" + names +
                    ", inner=" + inner +
                    '}';
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
