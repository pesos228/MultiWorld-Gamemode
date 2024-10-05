package org.plugin.world.impl;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.plugin.world.IWorldCreator;
import org.plugin.world.IWorldManager;

public class WorldCreatorImpl implements IWorldCreator {

    private final IWorldManager worldManager;
    private final Plugin plugin;

    public WorldCreatorImpl(IWorldManager worldManager, Plugin plugin) {
        this.worldManager = worldManager;
        this.plugin = plugin;
    }

    @Override
    public World createWorld(String name) {
        return null;
    }
}
