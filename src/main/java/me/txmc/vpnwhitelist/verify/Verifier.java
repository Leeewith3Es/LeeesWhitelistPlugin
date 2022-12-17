package me.txmc.vpnwhitelist.verify;

import me.txmc.vpnwhitelist.AntiBot;

import java.util.HashSet;

public abstract class Verifier {
    protected final AntiBot plugin;

    protected Verifier(AntiBot plugin) {
        this.plugin = plugin;
    }

    public abstract void verify(String name);

    public abstract int getAmount();

    public abstract void unVerify(String name);

    public abstract boolean isVerified(String name);


    public abstract void load();

    public abstract void save();
}
