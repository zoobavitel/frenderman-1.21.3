package net.zbavitel.frenderman.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class FrendermanConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/frenderman_config.json");

    public static FrendermanConfig INSTANCE;

    // Configurable fields
    public Map<String, Integer> tradeableItems = new HashMap<>(); // Item -> Trade chance
    public Map<String, Integer> roleChances = new HashMap<>(); // Role -> Percentage chance
    public int teleportDistance = 100; // Default teleport distance
    public int tradeLimit = 16; // Max trades per Enderman

    // Load configuration
    public static void load() {
        try {
            if (CONFIG_FILE.exists()) {
                INSTANCE = GSON.fromJson(new FileReader(CONFIG_FILE), FrendermanConfig.class);
            } else {
                INSTANCE = new FrendermanConfig();
                INSTANCE.initializeDefaults();
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            INSTANCE = new FrendermanConfig();
        }
    }

    // Save configuration
    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize default values
    private void initializeDefaults() {
        tradeableItems.put("minecraft:diamond", 10);
        tradeableItems.put("minecraft:emerald", 20);
        tradeableItems.put("minecraft:gold_ingot", 30);

        roleChances.put("merchant", 50);
        roleChances.put("guard", 30);
        roleChances.put("builder", 20);
    }
}
