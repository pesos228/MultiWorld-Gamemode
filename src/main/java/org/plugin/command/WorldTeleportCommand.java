package org.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;
import net.md_5.bungee.api.ChatColor;

public class WorldTeleportCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public WorldTeleportCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "❌ Пожалуйста, укажите название мира. Пример: /world-teleport <название мира>");
                return false;
            }

            String worldName = args[0];

            plugin.getLogger().info("Проверка существования мира с именем " + worldName);
            if (!worldService.isWorldExists(worldName)) {
                player.sendMessage(ChatColor.RED + "❌ Мир с именем '" + worldName + "' не существует.");
                return true;
            }

            // Попробуем загрузить мир, если он не загружен
            World targetWorld = Bukkit.getWorld(worldName);
            if (targetWorld == null) {
                plugin.getLogger().info("Мир " + worldName + " существует, но не загружен. Попытка загрузки...");
                targetWorld = Bukkit.createWorld(new WorldCreator(worldName));
            }

            if (targetWorld != null) {
                player.teleport(targetWorld.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "✨ Вы были телепортированы в мир '" + ChatColor.GOLD + worldName + ChatColor.GREEN + "'! 🌟");
                plugin.getLogger().info("Игрок " + player.getName() + " был телепортирован в мир '" + worldName + "'.");
            } else {
                player.sendMessage(ChatColor.RED + "❌ Не удалось найти или загрузить мир. Пожалуйста, попробуйте снова.");
                plugin.getLogger().warning("Не удалось найти или загрузить мир '" + worldName + "' для игрока " + player.getName());
            }

            return true;
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }
}
