package de.cycodly.worldsystem.gui.clicklistener;

import de.cycodly.worldsystem.guicreate.OrcClickListener;
import de.cycodly.worldsystem.guicreate.OrcInventory;
import de.cycodly.worldsystem.guicreate.OrcItem;
import org.bukkit.entity.Player;

public class ComingSoonClickListener implements OrcClickListener {

    @Override
    public void onClick(Player p, OrcInventory inv, OrcItem item) {
        p.closeInventory();
        p.sendMessage("§cComing soon...");
    }

}
