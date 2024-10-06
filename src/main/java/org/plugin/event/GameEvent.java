package org.plugin.event;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public interface GameEvent {
    String getName();
    void execute(JavaPlugin plugin, World world);
}
