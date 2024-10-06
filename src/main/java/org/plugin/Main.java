package org.plugin;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldCopyService;
import org.plugin.Service.WorldService;
import org.plugin.event.*;
import org.plugin.handler.*;

public class Main extends JavaPlugin implements Listener {
    private static final long VOTING_DURATION_TICKS = 1200L; // 1 минута
    private static final long VOTING_INTERVAL_TICKS = 12000L; // 10 минут

    private WorldService worldService;
    private WorldCopyService worldCopyService;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeServices();
        registerEventHandlers();
        setupLobby();
        scheduleVotingTasks();
    }

    private void initializeServices() {
        ServiceFactory serviceFactory = new ServiceFactory(this);
        worldService = serviceFactory.createWorldService();
        worldCopyService = serviceFactory.createWorldCopyService();
        eventManager = new EventManager(this, worldService);
    }

    private void registerEventHandlers() {
        PlayerEventHandler playerEventHandler = new PlayerEventHandler(this, worldService);
        MenuClickListener menuClickListener = new MenuClickListener(this, worldService);
        CommandManager commandManager = new CommandManager(this, worldService);

        commandManager.registerCommands();

        getServer().getPluginManager().registerEvents(playerEventHandler, this);
        getServer().getPluginManager().registerEvents(menuClickListener, this);
        getServer().getPluginManager().registerEvents(new LobbyProtectionListener(this), this);

        getServer().getPluginManager().registerEvents(new VotingMenuClickListener(this, eventManager), this);
        getServer().getPluginManager().registerEvents(new NewPlayerJoinListener(this), this);

    }

    private void setupLobby() {
        String lobbyWorldName = getConfig().getString("lobby-world", "lobby");
        if (!worldService.isWorldExists(lobbyWorldName)) {
            getLogger().info("Мир '" + lobbyWorldName + "' не найден. Копируем из ресурсов...");
            if (worldCopyService.copyWorldFromResources(lobbyWorldName)) {
                loadLobbyWorld(lobbyWorldName);
            } else {
                getLogger().warning("Не удалось скопировать мир '" + lobbyWorldName + "' из ресурсов.");
            }
        } else {
            loadLobbyWorld(lobbyWorldName);
        }
    }

    private void loadLobbyWorld(String lobbyWorldName) {
        World lobbyWorld = worldService.loadWorld(lobbyWorldName);
        if (lobbyWorld != null) {
            getLogger().info("Мир '" + lobbyWorldName + "' успешно загружен.");
        } else {
            getLogger().warning("Не удалось загрузить мир '" + lobbyWorldName + "'.");
        }
    }

    private void scheduleVotingTasks() {
        getServer().getScheduler().runTaskTimer(this, () -> {
            eventManager.startVoting();
            getServer().getScheduler().runTaskLater(this, eventManager::endVoting, VOTING_DURATION_TICKS);
        }, 0L, VOTING_INTERVAL_TICKS);
    }
}
