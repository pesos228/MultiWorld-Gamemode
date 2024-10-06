package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.event.GameEvent;

import java.util.Random;

public class RandomMobSpawnEvent implements GameEvent {
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Случайные Любимчики";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        int radius = random.nextInt(51); // Случайный радиус до 50 блоков
        int mobCount = random.nextInt(11); // Случайное количество мобов до 10

        EntityType[] mobTypes = {
                EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER,
                EntityType.ENDERMAN, EntityType.WITCH, EntityType.SLIME, EntityType.GHAST,
                EntityType.BLAZE, EntityType.MAGMA_CUBE, EntityType.PIGLIN, EntityType.HOGLIN,
                EntityType.ZOMBIFIED_PIGLIN, EntityType.PHANTOM, EntityType.DROWNED, EntityType.HUSK,
                EntityType.STRAY, EntityType.WITHER_SKELETON, EntityType.VINDICATOR, EntityType.EVOKER,
                EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VEX, EntityType.SHULKER,
                EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.SILVERFISH, EntityType.ENDERMITE,
                EntityType.POLAR_BEAR, EntityType.WOLF
        };

        for (int i = 0; i < mobCount; i++) {
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;
            int y = world.getHighestBlockYAt(x, z);

            Location location = new Location(world, x, y, z);
            EntityType mobType = mobTypes[random.nextInt(mobTypes.length)];

            LivingEntity entity = (LivingEntity) world.spawnEntity(location, mobType);
            entity.setCustomName("АНЯНЯ");
        }
    }
}
