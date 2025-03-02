package de.cycodly.worldsystem.event;

import de.cycodly.worldsystem.wrapper.SystemWorld;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

/**
 * Event when a world gets reset
 *
 * @author Butzlabben
 * @since 09.05.2018
 */
public class WorldResetEvent extends WorldEvent {

    public final static HandlerList handlers = new HandlerList();
    private final SystemWorld world;
    private final CommandSender executor;

    public WorldResetEvent(CommandSender executor, SystemWorld world) {
        this.executor = executor;
        this.world = world;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return world which gets reset
     */
    public SystemWorld getWorld() {
        return world;
    }

    /**
     * @return Executor of the command
     */
    public CommandSender getExecutor() {
        return executor;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }
}
