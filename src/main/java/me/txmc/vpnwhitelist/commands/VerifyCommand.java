package me.txmc.vpnwhitelist.commands;

import me.txmc.vpnwhitelist.AntiBot;
import me.txmc.vpnwhitelist.util.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class VerifyCommand extends Command {

    private final AntiBot plugin;

    public VerifyCommand(AntiBot plugin) {
        super("verify");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("l2x9antibot.whitelist")) {
            if (args.length > 0) {
                String target = args[0].trim();
                if (!plugin.getVerifier().isVerified(target)) {
                    plugin.getVerifier().verify(target);
                    Utils.sendMessage(sender, "&3Verified&r&a %s&r&3 successfully");
                } else Utils.sendMessage(sender, "&cThe player&r&a %s&r&c is already verified", target);
            } else Utils.sendMessage(sender, "&cUsage: /verify <player name>");
        } else Utils.sendMessage(sender, "&cNo permission");
    }
}
