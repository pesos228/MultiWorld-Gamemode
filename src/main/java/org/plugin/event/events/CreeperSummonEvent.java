package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.event.GameEvent;

import java.util.Random;

public class CreeperSummonEvent implements GameEvent {
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Любимчики";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        Location spawnLocation = world.getSpawnLocation();
        for (int i = 0; i < 10; i++) {
            Location creepLocation = getRandomLocationNearSpawn(spawnLocation);
            world.spawnEntity(creepLocation, EntityType.CREEPER);
        }
    }

    private Location getRandomLocationNearSpawn(Location spawn) {
        int x = spawn.getBlockX() + random.nextInt(21) - 10;
        int z = spawn.getBlockZ() + random.nextInt(21) - 10;
        int y = spawn.getWorld().getHighestBlockYAt(x, z);
        return new Location(spawn.getWorld(), x, y, z);
    }
}
