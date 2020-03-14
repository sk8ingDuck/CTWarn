package de.sk8ingduck.commands;

import de.sk8ingduck.mysql.MySQL;
import de.sk8ingduck.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Timestamp;

public class WarnCommand extends Command {

    private MySQL sql;

    public WarnCommand(MySQL sql) {
        super("warn");
        this.sql = sql;
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof ProxiedPlayer && !cs.hasPermission("warn.admin")) {
            cs.sendMessage(new TextComponent("§cKeine Berechtigung."));
            return;
        }

        if (args.length > 2 && args[0].equalsIgnoreCase("add")) {
            String name = args[1];
            String warnedBy = (cs instanceof ProxiedPlayer) ? ((ProxiedPlayer) cs).getUniqueId().toString() : "CONSOLE";
            StringBuilder reason = new StringBuilder();
            for (int i = 2; i < args.length; i++)
                reason.append(args[i]).append(" ");
            reason.deleteCharAt(reason.length() - 1);
            Timestamp date = new Timestamp(System.currentTimeMillis());

            UUIDFetcher.getUUIDAsync(name, uuid -> {
                if (uuid == null) {
                    cs.sendMessage(new TextComponent("§cSpieler nicht gefunden."));
                } else {
                    sql.executeAsync(String.format("INSERT INTO `warns`(`uuid`,`warnedBy`, `reason`, `date`) VALUES ('%s','%s','%s','%s')", uuid, warnedBy, reason, date),
                            result -> cs.sendMessage(new TextComponent(String.format("§9%s §awurde wegen §9%s §averwarnt.", name, reason))));
                }
            });
            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            String name = args[1];
            try {
                int id = Integer.parseInt(args[2]);
                UUIDFetcher.getUUIDAsync(name, uuid -> {
                    if (uuid == null) {
                        cs.sendMessage(new TextComponent("§cSpieler nicht gefunden."));
                    } else {
                        sql.updateAsync(String.format("DELETE FROM `warns` WHERE uuid='%s' AND warn=%d", uuid, id),
                                result -> cs.sendMessage(new TextComponent("§a" + result + " Verwarnung gelöscht.")));
                    }
                });

            } catch (NumberFormatException nfe) {
                cs.sendMessage(new TextComponent("§cID muss ein Integer sein."));
            }
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
            String name = args[1];
            UUIDFetcher.getUUIDAsync(name, uuid -> {
                if (uuid == null) {
                    cs.sendMessage(new TextComponent("§cSpieler nicht gefunden."));
                } else {
                    sql.queryAsync(String.format("SELECT * FROM `warns` WHERE uuid='%s'", uuid),
                            warnings -> warnings.forEach(warning -> warning.printWarning(cs)));
                }
            });
            return;
        }
        cs.sendMessage(new TextComponent("§e/warn add <player> <reason>\n" +
                "§e/warn remove <player> <warnID>\n" +
                "§e/warn list <player>"));
    }
}
