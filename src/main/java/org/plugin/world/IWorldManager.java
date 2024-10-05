package org.plugin.world;

import org.bukkit.World;

public interface IWorldManager {
    World getWorld(String name);
    boolean isWorldLoaded(String name);
    World loadWorld(String name);
    boolean isWorldExists(String name);
}
