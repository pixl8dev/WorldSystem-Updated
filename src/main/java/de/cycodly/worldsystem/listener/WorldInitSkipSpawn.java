package de.cycodly.worldsystem.listener;

import de.cycodly.worldsystem.config.SettingsConfig;
import de.cycodly.worldsystem.wrapper.SystemWorld;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.GameRule;

public class WorldInitSkipSpawn implements Listener {

    @EventHandler
    public void worldInit(WorldInitEvent e) {
        World world = e.getWorld();
        world.getWorldBorder().setWarningDistance(0);
        SystemWorld sw = SystemWorld.getSystemWorld(world.getName());
        if (sw == null) {
            return;
        }
        SettingsConfig.editWorld(world);
        world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
    }
}
