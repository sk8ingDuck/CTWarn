package de.sk8ingduck;

import de.sk8ingduck.commands.WarnCommand;
import de.sk8ingduck.events.Join;
import de.sk8ingduck.mysql.MySQL;
import de.sk8ingduck.utils.Config;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;


public class CTWarn extends Plugin {

    public void onEnable() {
        Config config = new Config();
        MySQL sql = new MySQL(this, config.getMySQLCredentials());

        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

        pluginManager.registerCommand(this, new WarnCommand(sql));
        pluginManager.registerListener(this, new Join(sql));
    }
}
