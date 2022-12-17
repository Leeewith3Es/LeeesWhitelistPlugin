package me.txmc.vpnwhitelist.listeners;

import lombok.RequiredArgsConstructor;
import me.txmc.vpnwhitelist.AntiBot;
import me.txmc.vpnwhitelist.util.Utils;
import me.txmc.vpnwhitelist.verify.KauriVPNVerifier;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class PlayerJoin implements Listener {
    private final AntiBot plugin;

    @EventHandler
    public void onJoin(PreLoginEvent event) {
        if (plugin.getVerifier() instanceof KauriVPNVerifier) return;
        if (!plugin.getVerifier().isVerified(event.getConnection().getName())) {
            TextComponent message = new TextComponent(Utils.translateColorCodes(String.join("\n", plugin.getConfig().getStringList("Kick-Message"))));
            event.setCancelReason(message);
            event.setCancelled(true);
        }
    }
}