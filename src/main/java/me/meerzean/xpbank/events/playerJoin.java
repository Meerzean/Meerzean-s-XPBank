package me.meerzean.xpbank.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class playerJoin implements Listener {

    @EventHandler
    public void pjoin(PlayerJoinEvent e) {
        playerjoin(e.getPlayer());
    }


    public void playerjoin(Player pl) {
        File folder = new File("plugins/XPBank/playerdata");
        folder.mkdirs();
        File playerFile = new File(folder, pl.getName() + ".yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                yaml.createSection("xpInBank");
                yaml.createSection("xpInBank.xp");
                List<Integer> values = new ArrayList<Integer>();
                values.add(0);
                yaml.set("xpInBank.xp", 0);
                try {
                    yaml.save("plugins/XPBank/playerdata/" + pl.getName() + ".yml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(pl.getName() + " (" + pl.getUniqueId() + ") joined. creating data file.");
        } else {
            System.out.println(pl.getDisplayName() + " already have a xpbank data file.");
        }
    }


}
