package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

import java.util.Random;

public class LavaRainEvent implements GameEvent {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "Теплый душ";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        int radius = random.nextInt(51); // Случайный радиус до 50 блоков
        int lavaCount = random.nextInt(11); // Случайное количество блоков лавы до 10

        for (int i = 0; i < lavaCount; i++) {
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;
            int y = world.getHighestBlockYAt(x, z) + 10; // Появление лавы на 10 блоков выше самой высокой точки

            Location location = new Location(world, x, y, z);

            new BukkitRunnable() {
                @Override
                public void run() {
                    world.getBlockAt(location).setType(Material.LAVA);
                }
            }.runTaskLater(plugin, i * 20L); // Задержка между появлением блоков лавы
        }

        plugin.getLogger().info("Блоки лавы появились в мире " + world.getName() + " с радиусом " + radius + " и количеством " + lavaCount);
    }
}
