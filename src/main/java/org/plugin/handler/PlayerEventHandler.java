package org.plugin.handler;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;

public class PlayerEventHandler implements Listener {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public PlayerEventHandler(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String lobbyWorldName = plugin.getConfig().getString("lobby-world", "lobby");
        World lobbyWorld = worldService.getWorld(lobbyWorldName);
        if (lobbyWorld != null) {
            player.teleport(lobbyWorld.getSpawnLocation());
            plugin.getLogger().info("Игрок " + player.getName() + " перенаправлен в мир 'lobby'.");
        } else {
            plugin.getLogger().warning("Мир 'lobby' не найден.");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();

        if (damager instanceof Player damagerPlayer && target instanceof Player targetPlayer) {

            String lobbyWorldName = plugin.getConfig().getString("lobby-world", "lobby");
            World lobbyWorld = worldService.getWorld(lobbyWorldName);

            if (damagerPlayer.getWorld().equals(lobbyWorld) && targetPlayer.getWorld().equals(lobbyWorld)) {
                event.setCancelled(true);
                damagerPlayer.sendMessage(ChatColor.RED + "Ой! " + ChatColor.YELLOW + "В " + ChatColor.GREEN + "lobby" + ChatColor.YELLOW + " драки запрещены! " + ChatColor.AQUA + "Попробуй лучше дружить! " + ChatColor.GOLD + "🤝");
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        World deathWorld = player.getWorld();

        if (!deathWorld.getEnvironment().equals(World.Environment.NETHER) && !deathWorld.getEnvironment().equals(World.Environment.THE_END)) {
            event.setRespawnLocation(deathWorld.getSpawnLocation());
            plugin.getLogger().info("Игрок " + player.getName() + " возродился в мире '" + deathWorld.getName() + "'.");
        } else {
            // Если игрок умер в Аду или Краю, возрождаем его в мире по умолчанию (например, в лобби)
            String lobbyWorldName = plugin.getConfig().getString("lobby-world", "lobby");
            World lobbyWorld = worldService.getWorld(lobbyWorldName);
            if (lobbyWorld != null) {
                event.setRespawnLocation(lobbyWorld.getSpawnLocation());
                plugin.getLogger().info("Игрок " + player.getName() + " возродился в мире 'lobby' после смерти в Аду или Краю.");
            } else {
                plugin.getLogger().warning("Мир 'lobby' не найден. Используется стандартное место возрождения.");
            }
        }
    }
}
