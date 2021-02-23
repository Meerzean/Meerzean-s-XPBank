package me.meerzean.xpbank.commands;

import me.meerzean.xpbank.XPBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class XPBankCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Configuration config = XPBank.plugin.getConfig();
        File folder = new File("plugins/XPBank/playerdata");
        File playerFile = new File(folder, player.getName() + ".yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
        int xpInBank = yaml.getInt("xpInBank.xp");



        if(args.length == 0) {

            if (config.getBoolean("use-xpbank-menu")) {
                String item = config.getString("menu-block");
                Inventory menu = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', config.getString("xp-bank-menu-title")));
                try {
                    ItemStack balance = new ItemStack(Material.valueOf(item));
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("error-wrong-item")));
                    return false;
                }
                ItemStack balance = new ItemStack(Material.valueOf(item));
                ArrayList<String> Lore = new ArrayList<String>();
                Lore.add("1");
                ItemMeta rename = balance.getItemMeta();
                rename.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("you-have-xp")).replaceFirst("%xp%", String.valueOf(xpInBank)));
                balance.setItemMeta(rename);
                menu.setItem(4, balance);
                player.openInventory(menu);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("you-have-xp")).replaceFirst("%xp%", String.valueOf(xpInBank)));
            }


        } else {


            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("xpbank.admin")) {
                    XPBank.plugin.reloadConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("reloaded-config")));
                } else {
                    player.sendMessage(config.getString("dont-have-permission"));
                }
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (player.hasPermission("xpbank.player"))
                    //add xp to bank
                    try {
                        Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("error-need-number")));
                        return false;
                    }
                int toAdd = Integer.parseInt(args[1]);
                if (player.getLevel() >= toAdd && toAdd > 0) {
                    player.setLevel(player.getLevel() - toAdd);
                    int inconfigxp = yaml.getInt("xpInBank.xp");
                    yaml.set("xpInBank.xp", inconfigxp + toAdd);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("success-add")).replaceFirst("%xp%", String.valueOf(toAdd)));
                    try {
                        yaml.save("plugins/XPBank/playerdata/" + player.getName() + ".yml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("error")).replaceFirst("%xp%", String.valueOf(toAdd)));
                }
            }


            if (args[0].equalsIgnoreCase("withdraw")) {
                try {
                    Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("error-need-number")));
                    return false;
                }
                int wantedXP = parseInt(args[1]);
                if (wantedXP <= xpInBank && wantedXP > 0) {
                    yaml.set("xpInBank.xp", xpInBank - wantedXP);
                    player.setLevel(player.getLevel() + wantedXP);
                    try {
                        yaml.save("plugins/XPBank/playerdata/" + player.getName() + ".yml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("success-withdraw")).replaceFirst("%xp%", String.valueOf(wantedXP)));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("error")));
                }
            }
        }
        return false;
    }
}
