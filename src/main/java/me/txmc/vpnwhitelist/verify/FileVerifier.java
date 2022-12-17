package me.txmc.vpnwhitelist.verify;

import me.txmc.vpnwhitelist.AntiBot;

import java.io.*;
import java.util.HashSet;
import java.util.stream.Collectors;

public class FileVerifier extends Verifier {
    private final File file;

    protected HashSet<String> verifiedNames;


    public FileVerifier(AntiBot plugin, File file) {
        super(plugin);
        this.file = file;
    }


    @Override
    public void verify(String name) {
        if (verifiedNames.contains(name)) return;
        verifiedNames.add(name);
    }

    @Override
    public int getAmount() {
        return verifiedNames.size();
    }

    @Override
    public void unVerify(String name) {
        verifiedNames.remove(name);
    }

    @Override
    public boolean isVerified(String name) {
        return verifiedNames.contains(name);
    }

    @Override
    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            verifiedNames = reader.lines().collect(Collectors.toCollection(HashSet::new));
            reader.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (String str : verifiedNames) {
                writer.write(str);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
