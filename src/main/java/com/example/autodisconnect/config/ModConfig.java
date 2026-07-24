package com.example.autodisconnect.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "autodisconnect.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enabled = true;
    public int radius = 512;
    public boolean checkTabList = false;
    public List<String> whitelist = new ArrayList<>();

    public static ModConfig INSTANCE = load();

    public static ModConfig load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
