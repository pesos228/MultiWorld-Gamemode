package org.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;
import org.plugin.command.*;

public class CommandManager {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public CommandManager(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    public void registerCommands() {
        plugin.getCommand("lobby").setExecutor(new LobbyCommand(plugin, worldService));
        plugin.getCommand("world-create").setExecutor(new CreateWorldCommand(plugin, worldService));
        plugin.getCommand("world-reload").setExecutor(new WorldReloadCommand(plugin, worldService));
        plugin.getCommand("world-teleport").setExecutor(new WorldTeleportCommand(plugin, worldService));
        plugin.getCommand("world-list").setExecutor(new WorldListCommand(plugin, worldService));
        plugin.getCommand("world-menu").setExecutor(new WorldMenuCommand(plugin, worldService));
    }
}
