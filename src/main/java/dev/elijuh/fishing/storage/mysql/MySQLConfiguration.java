package dev.elijuh.fishing.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.utils.YamlFile;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

/**
 * @author elijuh
 */
public class MySQLConfiguration {
    private final YamlFile config = new YamlFile(new File(Core.i().getDataFolder(), "mysql.yml"));

    public MySQLConfiguration() {
        config.addDefault("host", "127.0.0.1");
        config.addDefault("port", 3306);
        config.addDefault("database", Core.i().getDescription().getName().toLowerCase(Locale.ROOT));
        config.addDefault("user", "root");
        config.addDefault("password", "");
        config.addDefault("pool.maximum-pool-size", 10);
        config.addDefault("pool.minimum-idle", 10);
        config.addDefault("pool.maximum-lifetime", 180000);
        config.addDefault("pool.connection-timeout", 5000);
        config.addDefault("properties.useUnicode", true);
        config.addDefault("properties.characterEncoding", "utf8");
        config.save();
    }

    public HikariConfig toHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        String host = config.get("host");
        int port = config.get("port");
        String database = config.get("database");

        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setPoolName(Core.i().getDescription().getName().toLowerCase(Locale.ROOT) + "-pool");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(config.get("user"));
        hikariConfig.setPassword(config.get("password"));
        hikariConfig.setMaximumPoolSize(config.get("pool.maximum-pool-size"));
        hikariConfig.setMinimumIdle(config.get("pool.minimum-idle"));
        hikariConfig.setMaxLifetime(config.getConfig().getLong("pool.maximum-lifetime"));
        hikariConfig.setConnectionTimeout(config.getConfig().getLong("pool.connection-timeout"));
        if (config.get("properties") instanceof ConfigurationSection) {
            ConfigurationSection section = config.getConfig().getConfigurationSection("properties");
            Properties properties = new Properties();
            for (String key : section.getKeys(false)) {
                properties.setProperty(key, section.getString(key));
            }
            hikariConfig.setDataSourceProperties(properties);
        }

        return hikariConfig;
    }
}
