package org.plugin.event.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class DrinkEvent implements GameEvent {
    @Override
    public String getName() {
        return "Ить в тц...";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : world.getPlayers()) {
                    // Наложение эффектов
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 900, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 900, 30));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 900, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 900, 3));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 900, 3));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 900, 3));
                }

                // Задержка перед отправкой сообщения
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : world.getPlayers()) {
                            player.sendTitle("Ить в тц...", "", 10, 70, 20);
                        }
                    }
                }.runTaskLater(plugin, 100); // 100 тиков = 5 секунд
            }
        }.runTask(plugin);
    }
}
