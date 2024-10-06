package org.plugin.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class NewPlayerJoinListener implements Listener {

    private final Plugin plugin;

    public NewPlayerJoinListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            sendWelcomeMessages(player);
        }
    }

    private void sendWelcomeMessages(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.sendTitle(
                    ChatColor.GOLD + "✦ Добро пожаловать в MultiWorld ✦",
                    ChatColor.AQUA + "By VlaDick",
                    10, // fadeIn: 0.5 секунды
                    60, // stay: 3 секунды
                    10  // fadeOut: 0.5 секунды
            );
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
        });

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendTitle(
                    ChatColor.BLUE + "✦ Меню режима доступно по ✦",
                    ChatColor.GREEN + "/world-menu",
                    10, // fadeIn: 0.5 секунды
                    60, // stay: 3 секунды
                    10  // fadeOut: 0.5 секунды
            );
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
        }, 60L);

        // Сообщение 3: "Удачной игры!" через 6 секунд
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendTitle(
                    ChatColor.GREEN + "✦ Удачной игры! ✦",
                    "",
                    10, // fadeIn: 0.5 секунды
                    60, // stay: 3 секунды
                    10  // fadeOut: 0.5 секунды
            );
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
        }, 120L);
    }
}
