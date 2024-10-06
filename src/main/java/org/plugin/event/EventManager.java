package org.plugin.event;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.Service.WorldService;
import org.plugin.event.events.*;

import java.util.*;

public class EventManager {
    private final JavaPlugin plugin;
    private final WorldService worldService;
    private final List<GameEvent> events;
    private final Map<Player, Integer> votes;
    private List<GameEvent> currentEvents;
    private boolean votingActive;
    private final int minPlayers;
    private final boolean eventsEnabled;

    public EventManager(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
        this.events = new ArrayList<>();
        this.votes = new HashMap<>();
        this.votingActive = false;
        this.minPlayers = plugin.getConfig().getInt("voting.min-players", 3);
        this.eventsEnabled = plugin.getConfig().getBoolean("events.enabled", true);
        this.currentEvents = new ArrayList<>();
        initializeEvents();
    }

    private void initializeEvents() {
        if (!eventsEnabled) {
            plugin.getLogger().info("Все события отключены через конфигурацию.");
            return;
        }

        addEventIfEnabled("AmiroEvent", new AmiroEvent());
        addEventIfEnabled("CoronavirusEvent", new CoronavirusEvent());
        addEventIfEnabled("CreeperSummonEvent", new CreeperSummonEvent());
        addEventIfEnabled("DrinkEvent", new DrinkEvent());
        addEventIfEnabled("HayuHaychikiEvent", new HayuHaychikiEvent());
        addEventIfEnabled("LavaRainEvent", new LavaRainEvent());
        addEventIfEnabled("MeteorShowerEvent", new MeteorShowerEvent());
        addEventIfEnabled("NeverNeverRabbitEvent", new NeverNeverRabbitEvent());
        addEventIfEnabled("RandomMobSpawnEvent", new RandomMobSpawnEvent());
        addEventIfEnabled("RandomRainEvent", new RandomRainEvent());
        addEventIfEnabled("RandomTimeChangeEvent", new RandomTimeChangeEvent());
        addEventIfEnabled("ZombieApocalypseEvent", new ZombieApocalypseEvent());
        addEventIfEnabled("ChernyachkaEvent", new ChernyachkaEvent());
    }

    private void addEventIfEnabled(String configKey, GameEvent event) {
        if (plugin.getConfig().getBoolean("events." + configKey + ".enabled", true)) {
            events.add(event);
            plugin.getLogger().info("Событие " + event.getName() + " добавлено.");
        } else {
            plugin.getLogger().info("Событие " + event.getName() + " отключено.");
        }
    }



    public List<GameEvent> getRandomEvents(int count) {
        List<GameEvent> shuffledEvents = new ArrayList<>(events);
        Collections.shuffle(shuffledEvents);
        return shuffledEvents.subList(0, Math.min(count, shuffledEvents.size()));
    }

    public void startVoting() {
        if (!eventsEnabled) {
            return;
        }

        if (Bukkit.getOnlinePlayers().size() < minPlayers) {
            Bukkit.broadcastMessage(ChatColor.RED + "❌ Недостаточно игроков для начала голосования. Необходимо минимум " + minPlayers + " игроков.");
            return;
        }
        startVotingWithCountdown();
    }

    private void startVotingWithCountdown() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "⏳ Голосование начнется через 3 секунды!");

        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (countdown > 0) {
                    sendCountdownMessage(countdown);
                    countdown--;
                } else {
                    sendStartMessage();
                    startVotingInternal();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20); // 20 тиков = 1 секунда
    }

    private void sendCountdownMessage(int countdown) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(String.valueOf(countdown), "", 10, 20, 10);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    private void sendStartMessage() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("Начинаем!", "", 10, 20, 10);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    private void startVotingInternal() {
        currentEvents = getRandomEvents(4);
        votes.clear();
        votingActive = true;
        Bukkit.broadcastMessage(ChatColor.GREEN + "✨ Начинается голосование за случайное событие! Откройте меню голосования, чтобы выбрать одно из следующих событий:");

        openVotingMenuForAllPlayers();
    }



    private void openVotingMenuForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            openVotingMenu(player);
        }
    }

    private void openVotingMenu(Player player) {
        Inventory votingMenu = Bukkit.createInventory(null, 27, "Голосование за событие");

        for (int i = 0; i < currentEvents.size(); i++) {
            ItemStack eventItem = new ItemStack(Material.PAPER);
            ItemMeta eventMeta = eventItem.getItemMeta();
            String eventName = currentEvents.get(i).getName();
            eventMeta.setDisplayName(eventName);
            eventItem.setItemMeta(eventMeta);
            votingMenu.setItem(10 + i * 2, eventItem);
        }

        player.openInventory(votingMenu);
    }


    public void vote(Player player, int eventIndex) {
        if (!votingActive) {
            player.sendMessage(ChatColor.RED + "❌ Голосование уже завершено.");
            return;
        }
        if (eventIndex < 0 || eventIndex >= currentEvents.size()) {
            player.sendMessage(ChatColor.RED + "❌ Неверный выбор события.");
            return;
        }
        votes.put(player, eventIndex);

        Bukkit.broadcastMessage(ChatColor.YELLOW + "🗳 " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " проголосовал за событие: " + ChatColor.GREEN + currentEvents.get(eventIndex).getName());
    }

    public void endVoting() {
        if (!votingActive) {
            return;
        }

        votingActive = false;
        if (votes.isEmpty()) {
            if (currentEvents.isEmpty()) {
                Bukkit.broadcastMessage(ChatColor.RED + "Ошибка: Нет доступных событий для выбора.");
                return;
            }

            Random random = new Random();
            GameEvent selectedEvent = currentEvents.get(random.nextInt(currentEvents.size()));

            Bukkit.broadcastMessage(ChatColor.YELLOW + "🏁 Голосование завершено! Никто не проголосовал. Случайным образом выбрано событие: " + ChatColor.GOLD + selectedEvent.getName());
            executeEvent(selectedEvent);
            return;
        }

        announceVotingResults();
    }


    private void announceVotingResults() {
        Map<Integer, Long> voteCounts = new HashMap<>();
        for (int vote : votes.values()) {
            voteCounts.put(vote, voteCounts.getOrDefault(vote, 0L) + 1);
        }

        List<Map.Entry<Integer, Long>> sortedVotes = new ArrayList<>(voteCounts.entrySet());
        sortedVotes.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Bukkit.broadcastMessage(ChatColor.GREEN + "🏁 Голосование завершено! Результаты:");
        for (Map.Entry<Integer, Long> entry : sortedVotes) {
            String eventName = currentEvents.get(entry.getKey()).getName();
            long count = entry.getValue();
            Bukkit.broadcastMessage(ChatColor.GOLD + "- " + eventName + ": " + count + " голосов");
        }

        if (!sortedVotes.isEmpty()) {
            int winningEventIndex = sortedVotes.get(0).getKey();
            GameEvent winningEvent = currentEvents.get(winningEventIndex);
            Bukkit.broadcastMessage(ChatColor.GREEN + "🎉 Победило событие: " + ChatColor.GOLD + winningEvent.getName());
            executeEvent(winningEvent);
        } else {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "🏁 Голосование завершено! Никто не проголосовал.");
        }
    }

    public void executeEvent(GameEvent event) {
        for (String worldName : worldService.getWorldNames()) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                event.execute(plugin, world);
            }
        }
    }

    public List<GameEvent> getCurrentEvents() {
        return currentEvents;
    }
}
