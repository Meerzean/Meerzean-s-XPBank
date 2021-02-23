package me.meerzean.xpbank;

import me.meerzean.xpbank.commands.XPBankCommand;
import me.meerzean.xpbank.events.menuListener;
import me.meerzean.xpbank.events.playerJoin;
import org.bukkit.plugin.java.JavaPlugin;

public final class XPBank extends JavaPlugin {


    public static XPBank plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new menuListener(), this);
        getServer().getPluginManager().registerEvents(new playerJoin(), this);
        saveDefaultConfig();
        getCommand("xpbank").setExecutor(new XPBankCommand());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
