package me.ranzeplay.mcgit.gui;

import me.ranzeplay.mcgit.managers.ArchiveManager;
import me.ranzeplay.mcgit.models.Archive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class ArchivesPanel implements InventoryHolder, Listener {
    public Inventory getInventory() {
        ArrayList<Archive> archivesList = null;
        try {
            archivesList = ArchiveManager.getAllArchives();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "All Archives");
        int i = 1;
        for (Archive archive : Objects.requireNonNull(archivesList)) {
            if (i >= 54) {
                break;
            }
            i++;

            ItemStack item = new ItemStack(Material.GREEN_WOOL, 1);
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta()).clone();
            meta.setDisplayName(archive.getArchiveId().toString());

            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Description: " + ChatColor.YELLOW + archive.getDescription());
            lore.add(ChatColor.GREEN + "Create Time: " + ChatColor.YELLOW + archive.getCreateTime());
            lore.add(ChatColor.GREEN + "Operator: " + ChatColor.YELLOW + archive.getPlayer().getName());
            lore.add(ChatColor.GREEN + "World Name: " + ChatColor.YELLOW + archive.getWorld());
            meta.setLore(lore);

            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("All Archives")) {
            Player player = (Player) event.getWhoClicked();
            String archiveId = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName();
            if (event.getClick().isLeftClick()) {
                player.closeInventory();
                player.performCommand("mcgit archive view" + archiveId);
            } else if (event.getClick().isRightClick()) {
                player.closeInventory();
                player.performCommand("mcgit rollback " + archiveId);
            }

            event.setCancelled(true);
        }
    }
}
