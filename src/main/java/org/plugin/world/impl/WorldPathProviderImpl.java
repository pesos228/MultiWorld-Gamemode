package org.plugin.world.impl;

import org.bukkit.Bukkit;
import org.plugin.world.IWorldPathProvider;
public class WorldPathProviderImpl implements IWorldPathProvider {

    @Override
    public String getWorldsFolderPath() {
        return Bukkit.getWorldContainer().getAbsolutePath();
    }
}
