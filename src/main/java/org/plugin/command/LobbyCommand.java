package org.plugin.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;

public class LobbyCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public LobbyCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String lobbyWorldName = plugin.getConfig().getString("lobby-world", "lobby");
            World lobbyWorld = worldService.getWorld(lobbyWorldName);

            if (lobbyWorld != null) {
                player.teleport(lobbyWorld.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "✨ Вы были телепортированы в " + ChatColor.GOLD + "lobby" + ChatColor.GREEN + " ✨");
            } else {
                player.sendMessage("Мир 'lobby' не найден.");
                plugin.getLogger().warning("Мир 'lobby' не найден.");
            }
            return true;
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }
}
