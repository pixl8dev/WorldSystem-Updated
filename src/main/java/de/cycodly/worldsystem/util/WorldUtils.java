package de.cycodly.worldsystem.util;

import de.cycodly.worldsystem.config.SettingsConfig;
import de.cycodly.worldsystem.wrapper.SystemWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldUtils {

    public static void reloadWorldSettings() {
        for (World w : Bukkit.getWorlds()) {
            SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
            if (sw != null && sw.isLoaded())
                SettingsConfig.editWorld(w);

        }
    }
}
