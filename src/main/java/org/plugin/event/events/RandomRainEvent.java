package org.plugin.event.events;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.event.GameEvent;

public class RandomRainEvent implements GameEvent {
    @Override
    public String getName() {
        return "Дождик";
    }

    @Override
    public void execute(JavaPlugin plugin, World world) {
        world.setStorm(true);
    }
}
