package de.sk8ingduck.utils;

import java.io.File;
import java.io.IOException;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

public class Config {

    public Config() {
        setupConfig();
    }

    private void setupConfig() {

        File ord = new File("plugins/CTWarn");
        if (!ord.exists()) {
            ord.mkdir();
        }

        File mysql = new File("plugins/CTWarn/mysql.yml");

        try {
            if (!mysql.exists()) {
                mysql.createNewFile();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }


        try {
            Configuration sql = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(mysql);

                if (!sql.contains("mysql.host")) {
                    sql.set("mysql.host", "host");
                    sql.set("mysql.port", 3306);
                    sql.set("mysql.database", "database");
                    sql.set("mysql.username", "username");
                    sql.set("mysql.password", "password");
                }

            ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).save(sql, mysql);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public MySQLCredentials getMySQLCredentials() {
        File mysql = new File("plugins/CTWarn/mysql.yml");

        try {
            Configuration cfg = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(mysql);
            String host = cfg.getString("mysql.host");
            int port = cfg.getInt("mysql.port");
            String database = cfg.getString("mysql.database");
            String username = cfg.getString("mysql.username");
            String password = cfg.getString("mysql.password");

            return new MySQLCredentials(host, port, database, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
