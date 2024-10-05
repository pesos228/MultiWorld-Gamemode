package org.plugin.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;
import net.md_5.bungee.api.ChatColor;

public class CreateWorldCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public CreateWorldCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String worldName = player.getName();

            plugin.getLogger().info("Проверка существования мира с именем " + worldName);
            if (worldService.isWorldExists(worldName)) {
                plugin.getLogger().info("Мир с именем " + worldName + " уже существует.");
                player.sendMessage(ChatColor.YELLOW + "🌍 Мир с вашим именем уже существует. Телепортируем вас туда... 🚀");

                // Попробуем загрузить мир, если он не загружен
                World existingWorld = Bukkit.getWorld(worldName);
                if (existingWorld == null) {
                    plugin.getLogger().info("Мир " + worldName + " существует, но не загружен. Попытка загрузки...");
                    existingWorld = Bukkit.createWorld(new WorldCreator(worldName));
                }

                if (existingWorld != null) {
                    existingWorld.setDifficulty(Difficulty.HARD); // Устанавливаем сложность мира на сложную
                    worldService.setWorldBorder(existingWorld); // Устанавливаем границы мира
                    player.teleport(existingWorld.getSpawnLocation());
                    plugin.getLogger().info("Игрок " + player.getName() + " был телепортирован в существующий мир '" + worldName + "'.");
                } else {
                    player.sendMessage(ChatColor.RED + "❌ Не удалось найти или загрузить существующий мир. Пожалуйста, попробуйте снова.");
                    plugin.getLogger().warning("Не удалось найти или загрузить существующий мир '" + worldName + "' для игрока " + player.getName());
                }
                return true;
            }

            player.sendMessage(ChatColor.YELLOW + "⏳ Начинается процесс создания вашего личного мира... 🛠");
            plugin.getLogger().info("Создание мира для игрока " + player.getName());

            // Используем метод createWorld из WorldService
            worldService.createWorld(player, worldName);

            return true;
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }
}
