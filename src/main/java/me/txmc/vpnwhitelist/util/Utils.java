package me.txmc.vpnwhitelist.util;

import com.sun.net.httpserver.HttpExchange;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.*;

public class Utils {
    public static void writeResponse(HttpExchange exchange, String response, int code) {
        try {
            exchange.sendResponseHeaders(code, response.length());
            OutputStream stream = exchange.getResponseBody();
            stream.write(response.getBytes());
            stream.flush();
            stream.close();
        } catch (IOException ioException) {
        }
    }

    public static void sendMessage(CommandSender player, String format, Object... args) {
        TextComponent component = new TextComponent(translateColorCodes(String.format(format, args)));
        player.sendMessage(component);
    }

    public static String toString(InputStream stream) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        String output = String.join("\n", reader.lines().toArray(String[]::new));
        isr.close();
        reader.close();
        return output;
    }

    public static String translateColorCodes(String textToTranslate) {//100% pasted from bukkit because this isnt in minnimessage for some reason
        char[] chars = textToTranslate.toCharArray();

        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) {
                chars[i] = 167;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
}
