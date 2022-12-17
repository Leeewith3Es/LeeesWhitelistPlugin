package me.txmc.vpnwhitelist.commands;

import com.google.common.reflect.TypeToken;
import me.txmc.vpnwhitelist.AntiBot;
import me.txmc.vpnwhitelist.util.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnVerifyCommand extends Command {
    private final AntiBot plugin;

    public UnVerifyCommand(AntiBot plugin) {
        super("unverify");
        this.plugin = plugin;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("l2x9antibot.unwhitelist")) {
            if (args.length > 0) {
                String name = args[0].trim();
                if (plugin.getVerifier().isVerified(name)) {
                    TextComponent message = new TextComponent(Utils.translateColorCodes(String.join("\n", plugin.getConfig().getStringList ("Kick-Message"))));
                    ProxiedPlayer player = plugin.getServer().getPlayer(name);
                    if (player != null) player.disconnect(message);
                    plugin.getVerifier().unVerify(name);
                    Utils.sendMessage(sender, "&3Unverified&r&a %s", name);
                } else Utils.sendMessage(sender, "&cThe player&r&a %s&r&c is not verified", name);
            } else Utils.sendMessage(sender, "&cUsage: /unverify <player name>");
        } else Utils.sendMessage(sender, "&cNo permission");
    }
}
