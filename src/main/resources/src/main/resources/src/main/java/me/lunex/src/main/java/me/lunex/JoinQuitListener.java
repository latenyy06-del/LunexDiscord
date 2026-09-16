package me.lunex;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class JoinQuitListener implements Listener {

    private final LunexDiscord plugin;

    public JoinQuitListener(LunexDiscord plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        String playerName =
                event.getPlayer().getName();

        String message =
                plugin.getMessage("join", playerName);

        plugin.sendDiscord(message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        String playerName =
                event.getPlayer().getName();

        String message =
                plugin.getMessage("quit", playerName);

        plugin.sendDiscord(message);
    }
}
