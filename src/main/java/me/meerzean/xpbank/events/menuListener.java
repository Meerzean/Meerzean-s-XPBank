package me.meerzean.xpbank.events;

import me.meerzean.xpbank.XPBank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class menuListener implements Listener {


    Configuration config = XPBank.plugin.getConfig();

    public int getPlayerXPAmount(Player player) {
        File folder = new File("plugins/XPBank/playerdata");
        File playerFile = new File(folder, player.getPlayer().getUniqueId() + ".yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
        int xpInBank = yaml.getInt("xpInBank.xp");
        return xpInBank;
    }


    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        String item = config.getString("menu-block");
        String menuName = ChatColor.translateAlternateColorCodes('&', config.getString("error-need-number"));
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("xp-bank-menu-title")))){
            e.setCancelled(true);
            if (e.getCurrentItem() == null){
                return;
            }else if (e.getCurrentItem().getType().equals(Material.valueOf(item))){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("menu-block-info")).replaceFirst("%xp%", String.valueOf(getPlayerXPAmount(p))));
                p.closeInventory();
        }
    }

}




}
