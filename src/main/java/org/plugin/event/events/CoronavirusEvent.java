package org.plugin.event.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.event.GameEvent;

public class CoronavirusEvent implements GameEvent {
    @Override
    public String getName() {
        return "Коронавирус";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        for (Player player : world.getPlayers()) {
            // Наложение эффекта слабости и темного экрана
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 1)); // 1200 тиков = 1 минута
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1));

            // Отправка сообщений в чат
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    if (count >= 6) { // 6 сообщений за минуту (каждые 10 секунд)
                        cancel();
                        return;
                    }

                    player.chat("Эгхкхк, хккх, ну и кашель...");
                    count++;
                }
            }.runTaskTimer(plugin, 0, 200); // 200 тиков = 10 секунд
        }
    }
}
