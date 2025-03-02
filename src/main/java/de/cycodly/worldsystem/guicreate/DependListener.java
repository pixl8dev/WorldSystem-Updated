package de.cycodly.worldsystem.guicreate;

import de.cycodly.worldsystem.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DependListener {

    ItemStack getItemStack(Player p, WorldPlayer wp);

}
