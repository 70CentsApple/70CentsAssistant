package net.apple70cents.assistant70cents.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.apple70cents.assistant70cents.utils.LoggerUtils;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.util.Map;

/**
 * @author 70CentsApple
 */
public class ConfigStorage {
    public static final File FILE = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir()
                                                                                 .toFile(), "70CentsAssistant/config.json");

    private Map<String, Object> configMap;

    public static boolean configFileExists() {
        return FILE.exists();
    }

    public ConfigStorage(boolean useDefault) {
        readConfigFile(useDefault);
    }

    public Map getHashmap() {
        return configMap;
    }

    // combine two hashmaps
    public ConfigStorage withDefault(Map<String, Object> defaultMap) {
        for (Map.Entry<String, Object> entry : defaultMap.entrySet()) {
            // If there is a key conflict, the value of `configMap` takes precedence;
            // if there is no conflict, the key is added to `configMap`, and the value comes from `defaultMap`.
            configMap.putIfAbsent(entry.getKey(), entry.getValue());
        }
        configMap.put("config.version", defaultMap.get("config.version"));
        return this;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void readConfigFile(boolean loadDefault) {
        try {
            Reader reader;
            if (loadDefault) {
                reader = new InputStreamReader(MinecraftClient.getInstance().getClass().getClassLoader()
                                                              .getResourceAsStream("assets/assistant70cents/default_config.json"));
            } else {
                reader = new BufferedReader(new FileReader(FILE));
            }
            configMap = GSON.fromJson(reader, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(String variableName) {
        return configMap.get(variableName);
    }

    public void set(String variableName, Object value) {
        configMap.put(variableName, value);
    }

    public void save() {
        File folder = FILE.getParentFile();
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                LoggerUtils.error("[Assistant70Cents] Couldn't make config parent dir.");
            }
        }
        LoggerUtils.info("[Assistant70Cents] Saving configs.");
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(configMap, writer);
        } catch (Exception e) {
            LoggerUtils.error("[Assistant70Cents] Couldn't save config.");
            e.printStackTrace();
        }
    }
}
