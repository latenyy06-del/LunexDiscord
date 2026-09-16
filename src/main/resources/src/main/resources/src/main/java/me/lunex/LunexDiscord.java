package me.lunex;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class LunexDiscord extends JavaPlugin {

    private String webhook;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        webhook = getConfig().getString("webhook", "");

        if (webhook.isBlank() || webhook.equals("BURAYA_DISCORD_WEBHOOK")) {
            getLogger().warning("Discord webhook ayarlanmamis!");
        }

        Bukkit.getPluginManager().registerEvents(
                new JoinQuitListener(this),
                this
        );

        getLogger().info("LunexDiscord aktif!");
    }

    public void sendDiscord(String message) {

        if (webhook == null ||
                webhook.isBlank() ||
                webhook.equals("BURAYA_DISCORD_WEBHOOK")) {
            return;
        }

        Bukkit.getAsyncScheduler().runNow(this, task -> {

            try {
                HttpURLConnection connection =
                        (HttpURLConnection) URI.create(webhook)
                                .toURL()
                                .openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                String escapedMessage = message
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r");

                String json =
                        "{\"content\":\"" + escapedMessage + "\"}";

                try (OutputStream output =
                             connection.getOutputStream()) {

                    output.write(
                            json.getBytes(StandardCharsets.UTF_8)
                    );
                }

                int responseCode = connection.getResponseCode();

                if (responseCode < 200 || responseCode >= 300) {
                    getLogger().warning(
                            "Discord webhook hata kodu: "
                                    + responseCode
                    );
                }

                connection.disconnect();

            } catch (Exception exception) {

                getLogger().warning(
                        "Discord mesajı gönderilemedi: "
                                + exception.getMessage()
                );
            }
        });
    }

    public String getMessage(String type, String playerName) {

        String message = getConfig().getString(
                "messages." + type,
                ""
        );

        return message.replace(
                "%player%",
                playerName
        );
    }
}
