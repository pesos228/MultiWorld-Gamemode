package org.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

public class WorldReloadCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public WorldReloadCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String worldName = player.getName();
            World currentWorld = player.getWorld();

            if (!currentWorld.getName().equals(worldName)) {
                player.sendMessage(ChatColor.RED + "❌ Вы можете перезагрузить только свой личный мир.");
                return true;
            }

            player.sendMessage(ChatColor.YELLOW + "⏳ Начинается процесс перезагрузки вашего мира... 🛠");
            plugin.getLogger().info("Перезагрузка мира для игрока " + player.getName());

            // Телепортируем всех игроков в лобби перед удалением мира
            String lobbyWorldName = plugin.getConfig().getString("lobby-world", "lobby");
            World lobbyWorld = worldService.getWorld(lobbyWorldName);
            if (lobbyWorld != null) {
                for (Player p : currentWorld.getPlayers()) {
                    p.teleport(lobbyWorld.getSpawnLocation());
                    p.sendMessage(ChatColor.YELLOW + "🌍 Перезагрузка мира. Вы были телепортированы в лобби.");
                }
            }

            // Удаление мира
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (Bukkit.unloadWorld(currentWorld, false)) {
                    plugin.getLogger().info("Мир '" + worldName + "' был выгружен.");
                    if (deleteWorldFolder(currentWorld.getWorldFolder())) {
                        plugin.getLogger().info("Мир '" + worldName + "' был удален.");
                        // Создание нового мира
                        worldService.removeWorldName(worldName);
                        worldService.createWorld(player, worldName);
                    } else {
                        player.sendMessage(ChatColor.RED + "❌ Не удалось удалить старый мир. Пожалуйста, попробуйте снова.");
                        plugin.getLogger().warning("Не удалось удалить старый мир '" + worldName + "' для игрока " + player.getName());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "❌ Не удалось выгрузить старый мир. Пожалуйста, попробуйте снова.");
                    plugin.getLogger().warning("Не удалось выгрузить старый мир '" + worldName + "' для игрока " + player.getName());
                }
            });

            return true;
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }

    private boolean deleteWorldFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return path.delete();
    }
}
