package de.sk8ingduck.mysql;

import de.sk8ingduck.CTWarn;
import de.sk8ingduck.mysql.connection.Callback;
import de.sk8ingduck.mysql.connection.Connection;
import de.sk8ingduck.mysql.connection.ConnectionPool;
import de.sk8ingduck.mysql.connection.Pool;
import de.sk8ingduck.utils.MySQLCredentials;
import de.sk8ingduck.utils.Warning;
import jline.internal.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQL {

    private CTWarn plugin;

    private Pool<Connection> connectionPool;
    private Connection connection;

    public MySQL(CTWarn plugin, MySQLCredentials credentials) {
        this.plugin = plugin;
        this.connectionPool = new ConnectionPool(credentials);

        setupTables();
    }

    private void setupTables() {
        updateAsync("CREATE TABLE IF NOT EXISTS warns " +
                "(uuid VARCHAR(36) NOT NULL, " +
                "warn INT(11) NOT NULL AUTO_INCREMENT, " +
                "warnedBy VARCHAR(36) NOT NULL, " +
                "reason VARCHAR(255) NOT NULL, " +
                "date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (uuid, warn)) ENGINE=MyIsam", result -> {});
    }

    private void execute(Runnable run) {
        plugin.getProxy().getScheduler().runAsync(plugin, run);
    }

    private ArrayList<Warning> query(String statement) {
        ArrayList<Warning> warnings = new ArrayList<>();

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                warnings.add(new Warning(resultSet.getInt("warn"),
                        resultSet.getString("warnedBy"),
                        resultSet.getString("reason"),
                        resultSet.getString("date")));
            }

            resultSet.close();
            preparedStatement.close();
            connectionPool.checkIn(connection);

        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }

        return warnings;
    }

    public void queryAsync(String statement, @Nullable final Callback<ArrayList<Warning>> callback) {
        execute(() -> callback.call(query(statement)));
    }

    private int update(String statement) {
        int rows = -1;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            rows = preparedStatement.executeUpdate();

            preparedStatement.close();
            connectionPool.checkIn(connection);

        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }

        return rows;
    }

    public void updateAsync(String statement, @Nullable final Callback<Integer> callback) {
        execute(() -> callback.call(update(statement)));
    }

    private boolean execute(String statement) {
        boolean result = false;
        connection = connectionPool.checkOut();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            result = preparedStatement.execute();

            preparedStatement.close();
            connectionPool.checkIn(connection);

        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }

        return result;
    }

    public void executeAsync(String statement, @Nullable final Callback<Boolean> callback) {
        execute(() -> callback.call(execute(statement)));
    }
}