package net.hybrid.buildserver.menus;

import net.hybrid.core.utility.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MapsMenu implements Listener {

    public static Inventory getMapsMenu() {
        Inventory inv = Bukkit.createInventory(null, 54, "Hybrid Maps");

        ItemStack close = new ItemBuilder(Material.BARRIER).setDisplayName("&cClose").build();
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 0, (short) 15).setDisplayName(" ").build();



        inv.setItem(49, close);
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getView().getTitle().equalsIgnoreCase("Hybrid Maps")) {
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();



            if (displayName.equalsIgnoreCase("§cClose")) {
                player.closeInventory();
                player.updateInventory();
            }
        }
    }
}















