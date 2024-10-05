package org.plugin;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldCopyService;
import org.plugin.Service.WorldService;
import org.plugin.event.LobbyProtectionListener;
import org.plugin.event.MenuClickListener;
import org.plugin.event.PlayerEventHandler;


public class Main extends JavaPlugin implements Listener {
    private WorldService worldService;
    private WorldCopyService worldCopyService;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        ServiceFactory serviceFactory = new ServiceFactory(this);
        worldService = serviceFactory.createWorldService();
        worldCopyService = serviceFactory.createWorldCopyService();
        PlayerEventHandler playerEventHandler = new PlayerEventHandler(this, worldService);
        MenuClickListener menuClickListener = new MenuClickListener(this, worldService);
        commandManager = new CommandManager(this, worldService);

        commandManager.registerCommands();

        setupLobby();
        getServer().getPluginManager().registerEvents(playerEventHandler, this);
        getServer().getPluginManager().registerEvents(menuClickListener, this);
        getServer().getPluginManager().registerEvents(new LobbyProtectionListener(this), this);
    }

    private void setupLobby() {
        String lobbyWorldName = getConfig().getString("lobby-world", "lobby");
        if (!worldService.isWorldExists(lobbyWorldName)) {
            getLogger().info("Мир '" + lobbyWorldName + "' не найден. Копируем из ресурсов...");
            if (worldCopyService.copyWorldFromResources(lobbyWorldName)) {
                World lobbyWorld = worldService.loadWorld(lobbyWorldName);
                if (lobbyWorld != null) {
                    getLogger().info("Мир '" + lobbyWorldName + "' успешно скопирован и загружен.");
                } else {
                    getLogger().warning("Не удалось загрузить мир '" + lobbyWorldName + "' после копирования.");
                }
            } else {
                getLogger().warning("Не удалось скопировать мир '" + lobbyWorldName + "' из ресурсов.");
            }
        } else if (!worldService.isWorldLoaded(lobbyWorldName)) {
            World lobbyWorld = worldService.loadWorld(lobbyWorldName);
            if (lobbyWorld != null) {
                getLogger().info("Мир '" + lobbyWorldName + "' успешно загружен.");
            } else {
                getLogger().warning("Не удалось загрузить существующий мир '" + lobbyWorldName + "'.");
            }
        } else {
            getLogger().info("Мир '" + lobbyWorldName + "' уже загружен.");
        }
    }
}
