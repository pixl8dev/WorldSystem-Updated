package de.cycodly.worldsystem.database;

import de.cycodly.worldsystem.config.PluginConfig;
import lombok.Getter;

public class DataProvider {
    @Getter
    public static DataProvider instance = new DataProvider();

    @Getter
    public final IDataUtil util;

    private DataProvider() {
        String dbType = PluginConfig.getDatabaseType();
        if (dbType.equalsIgnoreCase("sqlite"))
            util = new SQliteConnector();
        else if (dbType.equalsIgnoreCase("mysql"))
            util = new MySQLConnector();
        else {
            throw new IllegalArgumentException("Unknown database type: " + dbType);
        }
    }
}