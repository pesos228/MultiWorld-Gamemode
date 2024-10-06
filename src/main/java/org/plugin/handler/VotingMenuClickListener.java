package org.plugin.handler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.event.EventManager;
import org.plugin.event.GameEvent;

import java.util.List;

public class VotingMenuClickListener implements Listener {
    private final JavaPlugin plugin;
    private final EventManager eventManager;

    public VotingMenuClickListener(JavaPlugin plugin, EventManager eventManager) {
        this.plugin = plugin;
        this.eventManager = eventManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Голосование за событие")) {
            return;
        }

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String clickedEventName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName().trim());
        Player player = (Player) event.getWhoClicked();

        List<GameEvent> currentEvents = eventManager.getCurrentEvents();

        int eventIndex = findEventIndex(currentEvents, clickedEventName);

        if (eventIndex != -1) {
            eventManager.vote(player, eventIndex);
        } else {
            player.sendMessage(ChatColor.RED + "❌ Не удалось найти событие: " + clickedEventName);
        }

        player.closeInventory();
    }

    private int findEventIndex(List<GameEvent> events, String clickedEventName) {
        for (int i = 0; i < events.size(); i++) {
            String currentEventName = ChatColor.stripColor(events.get(i).getName().trim());
            if (currentEventName.equalsIgnoreCase(clickedEventName)) {
                return i;
            }
        }
        return -1;
    }
}
