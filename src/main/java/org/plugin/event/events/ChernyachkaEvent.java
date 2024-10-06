package org.plugin.event.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class ChernyachkaEvent implements GameEvent {

    @Override
    public String getName() {
        return "Черничка";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location spawnLocation = world.getSpawnLocation().add(0, 1, 0); // Спавн на спавне мира

                // Спавн лягушки и установка атрибутов
                Frog frog = (Frog) world.spawnEntity(spawnLocation, EntityType.FROG);
                frog.setCustomName("Черничка");
                frog.setCustomNameVisible(true);
                frog.setGlowing(true);
                frog.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.99);
                frog.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(10.0);

                // Установка элитры
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                frog.getEquipment().setChestplate(elytra);

                // Установка предметов в руки лягушки
                ItemStack pumpkinPie = new ItemStack(Material.PUMPKIN_PIE);
                ItemMeta pieMeta = pumpkinPie.getItemMeta();
                pieMeta.setDisplayName("Тело Чернички");
                pumpkinPie.setItemMeta(pieMeta);
                frog.getEquipment().setItemInMainHand(pumpkinPie);
                frog.getEquipment().setItemInMainHandDropChance(0.85f);
                frog.getEquipment().setItemInOffHandDropChance(2.0f);
            }
        }.runTask(plugin);
    }
}
