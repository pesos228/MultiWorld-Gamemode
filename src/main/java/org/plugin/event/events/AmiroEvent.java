package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.MagmaCube;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class AmiroEvent implements GameEvent {

    @Override
    public String getName() {
        return "Амироголовый";
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

                for (int i = 0; i < 2; i++) { // Спавн 5 магма-кубов с эвокерами за раз
                    // Спавн магма-куба
                    MagmaCube magmaCube = (MagmaCube) world.spawnEntity(spawnLocation, EntityType.MAGMA_CUBE);
                    magmaCube.setSize(0);

                    // Спавн эвокера и установка атрибутов
                    Evoker evoker = (Evoker) world.spawnEntity(spawnLocation, EntityType.EVOKER);
                    evoker.setCustomName("Амироголовый");
                    evoker.setCustomNameVisible(true);
                    evoker.setHealth(1.0);
                    evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1.0);
                    evoker.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(10.0);

                    // Установка предметов в руки эвокера
                    evoker.getEquipment().setItemInMainHand(new ItemStack(Material.KELP, 3));
                    evoker.getEquipment().setItemInMainHandDropChance(0.5f);
                    evoker.getEquipment().setItemInOffHandDropChance(2.0f);

                    // Установка эвокера как пассажира магма-куба
                    magmaCube.addPassenger(evoker);
                }

                spawnCount++;
            }
        }.runTaskTimer(plugin, 0, 20 * 10); // Спавн каждые 10 секунд
    }
}
