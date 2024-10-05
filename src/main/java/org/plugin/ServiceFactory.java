package org.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldCopyService;
import org.plugin.Service.WorldService;
import org.plugin.world.IWorldCreator;
import org.plugin.world.IWorldManager;
import org.plugin.world.IWorldPathProvider;
import org.plugin.world.impl.WorldCreatorImpl;
import org.plugin.world.impl.WorldManagerImpl;
import org.plugin.world.impl.WorldPathProviderImpl;

public class ServiceFactory {
    private final JavaPlugin plugin;
    private final IWorldManager worldManager;
    private final IWorldCreator worldCreator;
    private final IWorldPathProvider worldPathProvider;

    public ServiceFactory(JavaPlugin plugin) {
        this.plugin = plugin;
        this.worldPathProvider = new WorldPathProviderImpl();
        this.worldManager = new WorldManagerImpl();
        this.worldCreator = new WorldCreatorImpl(worldManager, plugin);
    }

    public WorldService createWorldService() {
        return new WorldService(worldManager, plugin);
    }

    public WorldCopyService createWorldCopyService() {
        return new WorldCopyService(plugin);
    }
}