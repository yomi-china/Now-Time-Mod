package com.yomi.nowtime.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "nowtime.json");

    public static ModConfig config;

    public static void loadConfig() {
        try {
            if (!CONFIG_PATH.toFile().exists()) {
                createDefaultConfig();
            }

            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                config = GSON.fromJson(reader, ModConfig.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            createDefaultConfig();
        }
    }

    private static void createDefaultConfig() {
        config = new ModConfig();
        saveConfig();
    }

    public static void saveConfig() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ModConfig {
        public boolean useLocalTime = true;
        public String cronExpression = "0 */30 * * * ?";
        public String messageFormat = "Ding! Current time is: {time}";
    }
}