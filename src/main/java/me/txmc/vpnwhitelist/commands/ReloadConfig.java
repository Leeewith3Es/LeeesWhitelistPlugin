package me.txmc.vpnwhitelist.commands;

import me.txmc.vpnwhitelist.AntiBot;
import me.txmc.vpnwhitelist.util.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadConfig extends Command {
    private final AntiBot plugin;

    public ReloadConfig(AntiBot plugin) {
        super("abrl");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("l2x9antibot.reload")) {
            plugin.reloadConfig();
            Utils.sendMessage(sender, "&3Configuration reloaded successfully!");
        } else Utils.sendMessage(sender, "&cYou do not have permission to run this command");
    }
}
