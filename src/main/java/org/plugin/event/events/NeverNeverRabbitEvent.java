package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class NeverNeverRabbitEvent implements GameEvent {

    @Override
    public String getName() {
        return "НИКОГДА НИКОГДА";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        new BukkitRunnable() {
            int spawnCount = 0;
            final int maxSpawns = 2; // Максимальное количество спавнов

            @Override
            public void run() {
                if (spawnCount >= maxSpawns) {
                    cancel();
                    return;
                }

                Location spawnLocation = world.getSpawnLocation().add(0, 1, 0); // Спавн на спавне мира

                for (int i = 0; i < 2; i++) { // Спавн 5 кроликов за раз
                    Rabbit rabbit = (Rabbit) world.spawnEntity(spawnLocation, EntityType.RABBIT);
                    rabbit.setCustomName("НИКОГДА НИКОГДА");
                    rabbit.setCustomNameVisible(true);
                    rabbit.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
                    rabbit.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
                    rabbit.setHealth(100.0);
                    rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

                    // Установка предметов в руки кролика
                    rabbit.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND, 3));
                    rabbit.getEquipment().setItemInMainHandDropChance(0.5f);
                    rabbit.getEquipment().setItemInOffHandDropChance(2.0f);
                }

                spawnCount++;
            }
        }.runTaskTimer(plugin, 0, 20 * 10); // Спавн каждые 10 секунд
    }
}
