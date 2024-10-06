package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Turtle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class HayuHaychikiEvent implements GameEvent {

    @Override
    public String getName() {
        return "Хаю-Хайчики";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        new BukkitRunnable() {
            int spawnCount = 0;
            final int maxSpawns = 5; // Максимальное количество спавнов

            @Override
            public void run() {
                if (spawnCount >= maxSpawns) {
                    cancel();
                    return;
                }

                Location spawnLocation = world.getSpawnLocation().add(0, 1, 0); // Спавн на спавне мира

                for (int i = 0; i < 5; i++) { // Спавн 5 черепах за раз
                    Turtle turtle = (Turtle) world.spawnEntity(spawnLocation, EntityType.TURTLE);
                    turtle.setCustomName("Хаю хай");
                    turtle.setCustomNameVisible(true);
                    turtle.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(10.0);
                    turtle.getEquipment().setItemInMainHand(new ItemStack(Material.KELP, 3));
                    turtle.getEquipment().setItemInMainHandDropChance(0.5f);
                    turtle.getEquipment().setItemInOffHandDropChance(2.0f);
                }

                spawnCount++;
            }
        }.runTaskTimer(plugin, 0, 20 * 10); // Спавн черепах каждые 10 секунд
    }
}
