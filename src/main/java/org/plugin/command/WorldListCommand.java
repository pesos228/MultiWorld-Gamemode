package org.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class WorldListCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public WorldListCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<String> worldNames = worldService.getWorldNames();

            if (worldNames.isEmpty()) {
                player.sendMessage(ChatColor.RED + "❌ Нет созданных миров.");
            } else {
                player.sendMessage(ChatColor.GREEN + "🌍 Список созданных миров:");
                for (String worldName : worldNames) {
                    player.sendMessage(ChatColor.GOLD + "- " + worldName);
                }
            }
            return true;
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }
}
