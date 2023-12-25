package dev.elijuh.fishing.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author elijuh
 */
@Getter
public class YamlFile {
    private final YamlConfiguration config = new YamlConfiguration();
    private final File file;

    public YamlFile(File file) {
        this.file = file;
        create();
        reload();
    }

    public void setLocation(String path, Location location) {
        ConfigurationSection section = config.createSection(path);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", Math.round(location.getYaw()));
        section.set("pitch", Math.round(location.getPitch()));
    }

    public Location getLocation(String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return null;
        return new Location(Bukkit.getWorld(section.getString("world")),
            section.getDouble("x"),
            section.getDouble("y"),
            section.getDouble("z"),
            (float) section.getInt("yaw"),
            (float) section.getInt("pitch")
        );
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path, T def) {
        return (T) config.get(path, def);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public void create() {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                Bukkit.getLogger().warning("Failed to create directories: " + file.getPath());
            }
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Bukkit.getLogger().warning("Failed to create yml file: " + file.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDefault(String path, Object value) {
        if (!config.options().copyDefaults()) {
            config.options().copyDefaults(true);
        }
        config.addDefault(path, value);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}

