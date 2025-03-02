package de.cycodly.worldsystem.database;

import de.cycodly.worldsystem.config.PluginConfig;
import lombok.Getter;

public class DatabaseProvider {
    @Getter
    public static DatabaseProvider instance = new DatabaseProvider();

    @Getter
    public final DatabaseUtil util;

    private DatabaseProvider() {
        String dbType = PluginConfig.getDatabaseType();
        if (dbType.equalsIgnoreCase("sqlite"))
            util = new SqliteConnection();
        else if (dbType.equalsIgnoreCase("mysql"))
            util = new MysqlConnection();
        else {
            throw new IllegalArgumentException("Unknown database type: " + dbType);
        }
    }
}