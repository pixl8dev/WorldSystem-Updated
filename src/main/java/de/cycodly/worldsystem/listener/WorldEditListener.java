package de.cycodly.worldsystem.listener;

import de.cycodly.worldsystem.config.MessageConfig;
import de.cycodly.worldsystem.config.WorldConfig;
import de.cycodly.worldsystem.config.WorldPerm;
import de.cycodly.worldsystem.wrapper.WorldPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldEditListener implements Listener {

    private final List<String> worldeditcmds = new ArrayList<>();

    public WorldEditListener() {
        try {
            Class<?> serverClass = Class.forName("org.bukkit.craftbukkit.CraftServer");

            Field f1 = serverClass.getDeclaredField("commandMap");
            f1.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) f1.get(Bukkit.getServer());

            Field f2 = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f2.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) f2.get(commandMap);
            worldeditcmds.addAll(knownCommands.entrySet().stream()
                    .filter(entry -> entry.getKey().contains("worldedit"))
                    .map(entry -> entry.getValue().getName())
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void playerCommandHandler(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        if (!isWorldEditCommand(command)) {
            return;
        }
        Player p = event.getPlayer();
        String worldname = p.getWorld().getName();
        WorldPlayer wp = new WorldPlayer(p, worldname);
        if (wp.isOnSystemWorld() &&
                !wp.isOwnerofWorld() && !p.hasPermission(WorldPerm.WORLDEDIT.getOpPerm())) {
            WorldConfig wc = WorldConfig.getWorldConfig(p.getWorld().getName());
            if (!wc.hasPermission(p.getUniqueId(), WorldPerm.WORLDEDIT)) {
                p.sendMessage(MessageConfig.getNoPermission());
                event.setCancelled(true);
            }
        }
    }

    private boolean isWorldEditCommand(String command) {
        return worldeditcmds.contains(command)
                || worldeditcmds.contains(command.replaceFirst("/", ""))
                || worldeditcmds.contains(command.replaceFirst("/worldedit:", ""));
    }
}