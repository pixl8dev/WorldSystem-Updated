package de.cycodly.worldsystem.gui.clicklistener;

import de.cycodly.worldsystem.guicreate.OrcClickListener;
import de.cycodly.worldsystem.guicreate.OrcInventory;
import de.cycodly.worldsystem.guicreate.OrcItem;
import org.bukkit.entity.Player;

public class CommandExecutorClickListener implements OrcClickListener {

    private final String message;

    public CommandExecutorClickListener(String message) {
        this.message = message;
    }

    @Override
    public void onClick(Player p, OrcInventory inv, OrcItem item) {
        p.closeInventory();
        p.chat(message);
        // Fix for #9
        inv.redraw(p);
    }
}
