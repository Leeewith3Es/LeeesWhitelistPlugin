package me.txmc.vpnwhitelist.verify;

import me.txmc.vpnwhitelist.AntiBot;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;

/**
 * @author 254n_m
 * @since 2022/12/16 10:23 PM
 * This file was created as a part of VpnWhitelistWebsite
 */
public class KauriVPNVerifier extends Verifier {
    private final LuckPerms luckPerms;
    public KauriVPNVerifier(AntiBot plugin) {
        super(plugin);
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public void verify(String name) {
        String TrueCommand = plugin.getConfig().getString("TrueCommand");
        String cmd = String.format(TrueCommand, name);
        plugin.getServer().getPluginManager().dispatchCommand(plugin.getServer().getConsole(), cmd);
    }

    @Override
    public int getAmount() {
        return 420;
    }

    @Override
    public void unVerify(String name) {
        String FalseCommand = plugin.getConfig().getString("FalseCommand");
        String cmd = String.format(FalseCommand, name);
        plugin.getServer().getPluginManager().dispatchCommand(plugin.getServer().getConsole(), cmd);
    }

    @Override
    public boolean isVerified(String name) {
        User user = luckPerms.getUserManager().getUser(name);
        if (user == null) return false;
        return user.getNodes(NodeType.PERMISSION).stream().map(PermissionNode::getPermission).anyMatch(s -> s.equals("antivpn.bypass"));
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
