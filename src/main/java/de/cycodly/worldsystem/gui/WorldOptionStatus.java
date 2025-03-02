package de.cycodly.worldsystem.gui;

import de.cycodly.worldsystem.guicreate.DependListener;
import de.cycodly.worldsystem.guicreate.OrcItem;
import de.cycodly.worldsystem.config.DependenceConfig;
import de.cycodly.worldsystem.config.PluginConfig;
import de.cycodly.worldsystem.wrapper.WorldPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class WorldOptionStatus implements DependListener {

    public WorldOptionStatus(String statustype) {
        this.statustype = statustype;
    }

    @Override
    public ItemStack getItemStack(Player p, WorldPlayer wp) {
        String worldname = new DependenceConfig(p).getWorldname();
        File file = new File(worldname + "/worldconfig.yml");
        if (!file.exists())
            file = new File(PluginConfig.getWorlddir() + "/worldconfig.yml");
        if (!file.exists())
            return null;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (this.statustype == "tnt") {
            boolean b = cfg.getBoolean("Settings.TNTDamage");
        } 
        if (this.statustype == "fire") {
            boolean b = cfg.getBoolean("Settings.Fire");
        }
        if (b)
            return OrcItem.enabled.getItemStack(p);

        return null;
    }

}