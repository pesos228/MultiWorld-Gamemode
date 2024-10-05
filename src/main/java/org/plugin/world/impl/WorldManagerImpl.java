package org.plugin.world.impl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.plugin.world.IWorldManager;

import java.io.File;

public class WorldManagerImpl implements IWorldManager {
    @Override
    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    @Override
    public boolean isWorldLoaded(String name) {
        World world = Bukkit.getWorld(name);
        return world != null;
    }

    public boolean isWorldExists(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return true;
        }
        File worldFolder = new File(Bukkit.getWorldContainer(), name);
        return worldFolder.exists() && worldFolder.isDirectory();
    }

    @Override
    public World loadWorld(String name) {
        if (!isWorldLoaded(name)) {
            return Bukkit.createWorld(new WorldCreator(name));
        }
        return getWorld(name);
    }
}
