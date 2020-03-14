package de.sk8ingduck.events;

import de.sk8ingduck.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Join implements Listener {

    private MySQL sql;
    public Join(MySQL sql) {
        this.sql = sql;
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();

        sql.queryAsync(String.format("SELECT * FROM warns WHERE uuid='%s'", p.getUniqueId()),
                warns -> {
                    if(warns.size() != 0) {
                        ProxyServer.getInstance().getPlayers()
                                .stream()
                                .filter(player -> player.hasPermission("warn.admin"))
                                .forEach(player -> player.sendMessage(new TextComponent(
                                        "§c" + p.getName() + " §9hat §c" + warns.size() + " §9Verwarnungen!")));
                    }
                });
    }
}
