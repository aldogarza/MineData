package me.aldogarza.minedata;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class MainClass extends JavaPlugin {
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigManager configManager = new ConfigManager("db_config.yml");
        String url = configManager.getString("database.url");
        String username = configManager.getString("database.username");
        String password = configManager.getString("database.password");

        databaseManager = new DatabaseManager(url, username, password);
        try {
            databaseManager.connect();
            getLogger().info("Connected to the database successfully!");
        } catch (SQLException e) {
            getLogger().severe("Could not connect to the database!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            if (databaseManager != null) {
                databaseManager.disconnect();
                getLogger().info("Disconnected from the database successfully!");
            }
        } catch (SQLException e) {
            getLogger().severe("Could not disconnect from the database!");
            e.printStackTrace();
        }
    }
}