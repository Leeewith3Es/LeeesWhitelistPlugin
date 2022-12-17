package me.txmc.vpnwhitelist.webserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.txmc.vpnwhitelist.AntiBot;
import me.txmc.vpnwhitelist.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Pages implements HttpHandler {
    //This is the index
    private String index;

    public Pages(AntiBot plugin) {
        try {
            File index = plugin.getFiles()[0];
            InputStream fileAsStream = new FileInputStream(index);
            this.index = Utils.toString(fileAsStream);
            fileAsStream.close();
        } catch (Throwable t) {
        }
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.writeResponse(exchange, index, 200);
    }

    //This handles verifying the player
    public static class Submit implements HttpHandler {
        private final AntiBot plugin;
        private String notFound;
        private String passed;
        private String failed;

        public Submit(AntiBot antiBot) {
            plugin = antiBot;
            try {
                File notFound = plugin.getFiles()[3];
                InputStream nfas = new FileInputStream(notFound);
                this.notFound = Utils.toString(nfas);
                nfas.close();
                File failed = plugin.getFiles()[2];
                InputStream failedStream = new FileInputStream(failed);
                this.failed = Utils.toString(failedStream);
                failedStream.close();
                File passed = plugin.getFiles()[1];
                InputStream passedStream = new FileInputStream(passed);
                this.passed = Utils.toString(passedStream);
                passedStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        private boolean isCaptchaValid(String secretKey, String response, String ip) {
            try {
                String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + response + "&remoteip=" + ip;
                InputStream stream = (new URL(url)).openStream();
                String raw = Utils.toString(stream);
                stream.close();
                JsonElement json = (new JsonParser()).parse(raw);
                return json.getAsJsonObject().get("success").getAsBoolean();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getRawQuery() == null) {
                Utils.writeResponse(exchange, notFound, 404);
            } else {
                String response;
                String[] rawReq = exchange.getRequestURI().getRawQuery().split("&");
                String username = rawReq[0].replace("username=", "").trim();
                if (username.length() > 16) {
                    Utils.writeResponse(exchange, failed, 200);
                    return;
                }
                String key = plugin.getConfig().getString("Secret-Key");
                String ip = (exchange.getRequestHeaders().containsKey("X-Forwarded-For")) ? exchange.getRequestHeaders().get("X-Forwarded-For").get(0).split(",")[0] : exchange.getLocalAddress().getAddress().getHostAddress();
                plugin.log("&3Player&r&a %s&r&3 verifying with ip&r&a %s", username, ip);
                String gCaptchaResponse = rawReq[1].replace("g-recaptcha-response=", "");
                if (isCaptchaValid(key, gCaptchaResponse, ip)) {
                    if (!plugin.getVerifier().isVerified(username)) {
                        plugin.getVerifier().verify(username);
                        plugin.log("&3Verified&r&a %s&r&3 from the web", username);
                        response = passed;
                    } else {
                        response = failed;
                    }
                } else {
                    response = failed;
                }
                Utils.writeResponse(exchange, response, 200);
            }
        }
    }
}