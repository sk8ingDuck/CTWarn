package de.sk8ingduck.mysql.connection;

import de.sk8ingduck.utils.MySQLCredentials;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool extends AbstractPool<Connection> {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public ConnectionPool(MySQLCredentials credentials) {
        this.host = credentials.getHost();
        this.port = credentials.getPort();
        this.database = credentials.getDatabase();
        this.username = credentials.getUsername();
        this.password = credentials.getPassword();
    }

    @Override
    public Connection create() {
        try {
            java.sql.Connection connection = DriverManager
                    .getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            return new Connection(this, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}