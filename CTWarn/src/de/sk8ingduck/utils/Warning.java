package de.sk8ingduck.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class Warning {

    private int id;
    private String warnedBy;
    private String reason;
    private String date;

    public Warning(int id, String warnedBy, String reason, String date) {
        this.id = id;
        this.warnedBy = warnedBy;
        this.reason = reason;
        this.date = date;
    }

    public void printWarning(CommandSender sender) {
        if(warnedBy.equals("CONSOLE")) {
            sender.sendMessage(new TextComponent("§6ID: §c" + id +
                    "\n§6Gewarnt von: §cCONSOLE" +
                    "\n§6Grund: §c" + reason +
                    "\n§6Datum: §c" + date));
            return;
        }
        UUIDFetcher.getNameAsync(UUID.fromString(warnedBy), warnedBy ->
                sender.sendMessage(new TextComponent("§6ID: §c" + id +
                "\n§6Gewarnt von: §c" + warnedBy +
                "\n§6Grund: §c" + reason +
                "\n§6Datum: §c" + date)));
    }
}
