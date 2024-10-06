package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

import java.util.Random;

public class MeteorShowerEvent implements GameEvent {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "Метеоритный дождь";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        Location spawnLocation = world.getSpawnLocation();

        new BukkitRunnable() {
            int explosions = 0;

            @Override
            public void run() {
                if (explosions >= 10) { // 10 взрывов
                    cancel();
                    return;
                }

                Location meteorLocation = getRandomLocationNearSpawn(spawnLocation);
                world.createExplosion(meteorLocation, 6.0f, false, true); // Увеличиваем радиус взрыва до 6 блоков

                // Спавним больше огня
                for (int j = 0; j < 5; j++) {
                    Location fireLocation = meteorLocation.clone().add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
                    fireLocation.getBlock().setType(Material.FIRE);
                }

                explosions++;
            }
        }.runTaskTimer(plugin, 0, 20 * 1); // 20 тиков = 1 секунда
    }

    private Location getRandomLocationNearSpawn(Location spawn) {
        int x = spawn.getBlockX() + random.nextInt(101) - 50; // Радиус 50 блоков от спавна
        int z = spawn.getBlockZ() + random.nextInt(101) - 50;
        int y = spawn.getWorld().getHighestBlockYAt(x, z);
        return new Location(spawn.getWorld(), x, y, z);
    }
}
