package de.cycodly.worldsystem.wrapper;

import org.bukkit.WorldCreator;

/**
 * @author Butzlabben
 * @since 07.06.2018
 * @author Cycodly
 * @since 06.03.2025
 */
public interface ICreatorAdapter {

    public void create(WorldCreator creator, SystemWorld world, Runnable sendPlayerMessageCallback);
}