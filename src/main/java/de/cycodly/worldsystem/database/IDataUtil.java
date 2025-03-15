package de.cycodly.worldsystem.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDataUtil {

    public ResultSet executeQuery(PreparedStatement preparedStatement) throws SQLException;

    public int executeUpdate(PreparedStatement preparedStatement) throws SQLException;

    public PreparedStatement prepareStatement(String sql) throws SQLException;

    public void close();

    public void connect();

    public boolean isConnectionAvailable();
}
