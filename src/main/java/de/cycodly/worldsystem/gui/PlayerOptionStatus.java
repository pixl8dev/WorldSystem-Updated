package de.cycodly.worldsystem.gui.playeroption;

import de.cycodly.worldsystem.guicreate.DependListener;
import de.cycodly.worldsystem.guicreate.OrcItem;
import de.cycodly.worldsystem.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * BuildStatus
 * GamemodeStatus
 * TeleportStatus
 * WorldEditStatus
 */

public class PlayerOptionStatus implements DependListener {

    private final WorldPlayer wp;

    public PlayerOptionStatus(WorldPlayer wp, String statustype) {
        this.wp = wp;
        this.statustype = statustype;
    }

    @Override
    public ItemStack getItemStack(Player p, WorldPlayer player) {
        switch (this.statustype) {
            case "build":
                return wp.canBuild() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
                break;
            case "gamemode":
                return wp.canChangeGamemode() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
                break;
            case "teleport":
                return wp.canTeleport() ? OrcItem.enabled.getItemStack(p, wp) : OrcItem.disabled.getItemStack(p, wp);
                break;
            case "worldedit":
                return this.wp.canWorldedit() ? OrcItem.enabled.getItemStack(p, this.wp) : OrcItem.disabled.getItemStack(p, this.wp); // was without override before change
                break;
        
            default:
                break;
        }
    }
}