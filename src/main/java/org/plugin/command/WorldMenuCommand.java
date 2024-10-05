package org.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.Service.WorldService;

public class WorldMenuCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final WorldService worldService;

    public WorldMenuCommand(JavaPlugin plugin, WorldService worldService) {
        this.plugin = plugin;
        this.worldService = worldService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            openWorldMenu(player);
            return true;
        } else {
            sender.sendMessage("Эту команду могут использовать только игроки.");
            return false;
        }
    }

    private void openWorldMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "📜 Меню миров");

        ItemStack createWorldItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta createWorldMeta = createWorldItem.getItemMeta();
        createWorldMeta.setDisplayName("🛠 Создать мир");
        createWorldItem.setItemMeta(createWorldMeta);

        ItemStack teleportWorldItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta teleportWorldMeta = teleportWorldItem.getItemMeta();
        teleportWorldMeta.setDisplayName("🌍 Телепорт в мир");
        teleportWorldItem.setItemMeta(teleportWorldMeta);

        ItemStack reloadWorldItem = new ItemStack(Material.REDSTONE);
        ItemMeta reloadWorldMeta = reloadWorldItem.getItemMeta();
        reloadWorldMeta.setDisplayName("🔄 Перезагрузить мир");
        reloadWorldItem.setItemMeta(reloadWorldMeta);

        ItemStack lobbyItem = new ItemStack(Material.GREEN_BED);
        ItemMeta lobbyMeta = lobbyItem.getItemMeta();
        lobbyMeta.setDisplayName("🛏 Телепорт в лобби");
        lobbyItem.setItemMeta(lobbyMeta);

        menu.setItem(10, createWorldItem);
        menu.setItem(12, teleportWorldItem);
        menu.setItem(14, reloadWorldItem);
        menu.setItem(16, lobbyItem);

        player.openInventory(menu);
    }

}
