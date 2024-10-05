package org.plugin.Service;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.world.IWorldManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorldService {
    private final IWorldManager worldManager;
    private final JavaPlugin plugin;
    private final File worldsFile;

    public WorldService(IWorldManager worldManager, JavaPlugin plugin) {
        this.worldManager = worldManager;
        this.plugin = plugin;
        this.worldsFile = new File(plugin.getDataFolder(), "worlds.txt");
        if (!worldsFile.exists()) {
            try {
                worldsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать файл worlds.txt: " + e.getMessage());
            }
        }
    }

    public World getWorld(String name) {
        return worldManager.getWorld(name);
    }

    public boolean isWorldLoaded(String name) {
        return worldManager.isWorldLoaded(name);
    }

    public World loadWorld(String name) {
        return worldManager.loadWorld(name);
    }

    public boolean isWorldExists(String name){
        return worldManager.isWorldExists(name);
    }

    public void createWorld(Player player, String worldName) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator worldCreator = new WorldCreator(worldName);
            World newWorld = Bukkit.createWorld(worldCreator);

            if (newWorld != null) {
                newWorld.setDifficulty(Difficulty.HARD); // Устанавливаем сложность мира на сложную
                setWorldBorder(newWorld); // Устанавливаем границы мира
                player.teleport(newWorld.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "✨ Ваш личный мир '" + ChatColor.GOLD + worldName + ChatColor.GREEN + "' был создан и вы были телепортированы туда! 🌟");
                plugin.getLogger().info("Мир '" + worldName + "' был успешно создан для игрока " + player.getName());
                addWorldName(worldName); // Добавляем название мира в файл
            } else {
                player.sendMessage(ChatColor.RED + "❌ Не удалось создать мир. Пожалуйста, попробуйте снова.");
                plugin.getLogger().warning("Не удалось создать мир '" + worldName + "' для игрока " + player.getName());
            }
        });
    }

    public void setWorldBorder(World world) {
        WorldBorder border = world.getWorldBorder();
        border.setCenter(world.getSpawnLocation());
        border.setSize(100); // Устанавливаем границы мира 100x100 блоков
        border.setDamageAmount(1.0); // Устанавливаем урон при пересечении границы
        border.setWarningDistance(5); // Устанавливаем предупреждение за 5 блоков до границы
    }

    public void removeWorldName(String worldName) {
        List<String> worldNames = getWorldNames();
        if (worldNames.remove(worldName)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(worldsFile))) {
                for (String name : worldNames) {
                    writer.write(name);
                    writer.newLine();
                }
                plugin.getLogger().info("Мир '" + worldName + "' был удален из файла worlds.txt.");
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось обновить файл worlds.txt: " + e.getMessage());
            }
        } else {
            plugin.getLogger().warning("Мир '" + worldName + "' не найден в файле worlds.txt.");
        }
    }
    public void addWorldName(String worldName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(worldsFile, true))) {
            writer.write(worldName);
            writer.newLine();
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось записать название мира в файл: " + e.getMessage());
        }
    }

    public List<String> getWorldNames() {
        List<String> worldNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(worldsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                worldNames.add(line);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось прочитать файл worlds.txt: " + e.getMessage());
        }
        return worldNames;
    }
}
