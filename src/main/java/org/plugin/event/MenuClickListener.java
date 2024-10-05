package org.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;

import java.util.List;

public class MenuClickListener implements Listener {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public MenuClickListener(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory menu = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getView().getTitle().equals("📜 Меню миров")) {
            event.setCancelled(true); // Предотвращаем взятие предметов из меню

            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            String itemName = clickedItem.getItemMeta().getDisplayName();
            Player player = (Player) event.getWhoClicked();

            switch (itemName) {
                case "🛠 Создать мир" -> player.performCommand("world-create");
                case "🌍 Телепорт в мир" -> openWorldSelectionMenu(player);
                case "🔄 Перезагрузить мир" -> player.performCommand("world-reload");
                case "🛏 Телепорт в лобби" -> player.performCommand("lobby");
            }

            player.closeInventory();
        } else if (event.getView().getTitle().equals("🌍 Выбор мира")) {
            event.setCancelled(true); // Предотвращаем взятие предметов из меню

            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            String worldName = clickedItem.getItemMeta().getDisplayName().replaceFirst("🗺\\s*", "").trim();
            // Убираем эмодзи перед названием мира
            Player player = (Player) event.getWhoClicked();
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                if (worldService.isWorldExists(worldName)) {
                    world = worldService.loadWorld(worldName);
                } else {
                    player.sendMessage(ChatColor.RED + "❌ Не удалось найти или загрузить существующий мир. Пожалуйста, попробуйте снова.");
                    return;
                }
            }

            if (world != null) {
                player.teleport(world.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "✨ Вы были телепортированы в мир '" + ChatColor.GOLD + worldName + ChatColor.GREEN + "'! 🌟");
            } else {
                player.sendMessage(ChatColor.RED + "❌ Не удалось загрузить мир " + worldName);
            }

            player.closeInventory();
        }
    }

    private void openWorldSelectionMenu(Player player) {
        List<String> worldNames = worldService.getWorldNames();
        Inventory worldMenu = Bukkit.createInventory(null, 27, "🌍 Выбор мира");

        for (String worldName : worldNames) {
            ItemStack worldItem = new ItemStack(Material.PAPER);
            ItemMeta worldMeta = worldItem.getItemMeta();
            worldMeta.setDisplayName("🗺 " + worldName);
            worldItem.setItemMeta(worldMeta);
            worldMenu.addItem(worldItem);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(worldMenu), 1L);

        if (worldNames.isEmpty()) {
            player.sendMessage(ChatColor.RED + "❌ Нет созданных миров.");
        } else {
            player.sendMessage(ChatColor.GREEN + "🌍 Список созданных миров:");
        }
    }
}
