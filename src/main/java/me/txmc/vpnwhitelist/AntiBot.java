package me.txmc.vpnwhitelist;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import me.txmc.vpnwhitelist.commands.ReloadConfig;
import me.txmc.vpnwhitelist.commands.UnVerifyCommand;
import me.txmc.vpnwhitelist.commands.VerifyCommand;
import me.txmc.vpnwhitelist.listeners.PlayerJoin;
import me.txmc.vpnwhitelist.util.Utils;
import me.txmc.vpnwhitelist.verify.FileVerifier;
import me.txmc.vpnwhitelist.verify.KauriVPNVerifier;
import me.txmc.vpnwhitelist.verify.Verifier;
import me.txmc.vpnwhitelist.webserver.Pages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

@Getter
public final class AntiBot extends Plugin {
    private final File[] files = new File[4];
    private PluginManager pluginManager;
    private ProxyServer server;
    private HttpServer webServer;
    private Configuration config;
    private File configFile;
    private File verifiedFile;
    private Verifier verifier;


    @Override
    public void onEnable() {
        try {
            this.server = ProxyServer.getInstance();
            pluginManager = server.getPluginManager();
            loadConfig();
            verifiedFile = new File(getPluginDataFolder(), "verified.txt");
            if (!verifiedFile.exists()) verifiedFile.createNewFile();
            verifier = new KauriVPNVerifier(this);
            verifier.load();
            log("&3Loaded&r&a %d&r&3 names from the file", verifier.getAmount());
            loadHTML();
            int port = config.getInt("Port");
            webServer = HttpServer.create(new InetSocketAddress(port), 0);
            webServer.createContext("/", new Pages(this));
            webServer.createContext("/submit", new Pages.Submit(this));
            webServer.start();
            log("&3WebServer started on port&r&a %d&r", webServer.getAddress().getPort());
            pluginManager.registerListener(this, new PlayerJoin(this));
            pluginManager.registerCommand(this, new VerifyCommand(this));
            pluginManager.registerCommand(this, new UnVerifyCommand(this));
            pluginManager.registerCommand(this, new ReloadConfig(this));
        } catch (Throwable t) {
            log("&cFailed to enable plugin please see the stacktrace below for more info");
            t.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        verifier.save();
        webServer.stop(0);
        log("&3WebServer stopped");
    }


    public File getPluginDataFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }

    private void loadConfig() throws Throwable {
        configFile = new File(getPluginDataFolder(), "config.yml");
        if (!configFile.exists()) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("config.yml");
            if (is == null) throw new NullPointerException("Missing resource config.yml");
            Files.copy(is, configFile.toPath());
        }
        ConfigurationProvider loader = ConfigurationProvider.getProvider(YamlConfiguration.class);
        config = loader.load(configFile);
    }


    public void log(String format, Object... args) {
        getLogger().info(Utils.translateColorCodes(String.format(format, args)));
    }

    public void reloadConfig() {
        try {
            loadConfig();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void loadHTML() {
        try {
            File htmlFolder = new File(getPluginDataFolder(), "HTMLFiles");
            files[0] = new File(htmlFolder, "index.html");
            files[1] = new File(htmlFolder, "passed.html");
            files[2] = new File(htmlFolder, "failed.html");
            files[3] = new File(htmlFolder, "notfound.html");
            if (!htmlFolder.exists()) htmlFolder.mkdir();
            for (File file : getFiles()) {
                if (file.exists()) continue;
                InputStream is = getClass().getClassLoader().getResourceAsStream(file.getName());
                if (is == null) {
                    log("&cMissing resource&r&a %s&r&c in plugin jar");
                    continue;
                }
                Files.copy(is, file.toPath());
                is.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
