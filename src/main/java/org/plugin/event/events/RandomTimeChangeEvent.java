package org.plugin.event.events;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.event.GameEvent;

import java.util.Random;

public class RandomTimeChangeEvent implements GameEvent {
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Случайное изменение времени суток";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        long randomTime = random.nextInt(24000);
        world.setTime(randomTime);
    }
}
