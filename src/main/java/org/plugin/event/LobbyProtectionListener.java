package org.plugin.event;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyProtectionListener implements Listener {
    private final JavaPlugin plugin;

    public LobbyProtectionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        World world = event.getBlock().getWorld();
        if (world.getName().equalsIgnoreCase("lobby")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "❌ Вы не можете ломать блоки в lobby");
        }
    }
}
