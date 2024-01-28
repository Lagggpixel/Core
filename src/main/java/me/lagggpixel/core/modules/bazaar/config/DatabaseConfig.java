/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.config;

import me.lagggpixel.core.modules.bazaar.databases.SQLDatabase;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class DatabaseConfig extends CustomConfig {
    public DatabaseConfig(JavaPlugin plugin) {
        super(plugin, "database");
    }

    @Override
    protected void addDefaults() {
        addDefault("type", "file");
        addDefault("mysql.hostname", "");
        addDefault("mysql.port", 3306);
        addDefault("mysql.database", "");
        addDefault("mysql.username", "");
        addDefault("mysql.password", "");
    }

    public ConfigurationSection getMySQLConfig() {
        return getConfig().getConfigurationSection("mysql");
    }

    public SQLDatabase.Type getSqlType() {
        for (SQLDatabase.Type type : SQLDatabase.Type.values()) {
            if (!getConfig().getString("type").equalsIgnoreCase(type.getConfigName())) continue;
            return type;
        }
        return SQLDatabase.Type.SQLITE;
    }
}
