package de.cycodly.worldsystem.gui;

import de.cycodly.worldsystem.guicreate.DependListener;
import de.cycodly.worldsystem.guicreate.OrcInventory;
import de.cycodly.worldsystem.guicreate.OrcItem;
import de.cycodly.worldsystem.config.GuiConfig;
import de.cycodly.worldsystem.gui.clicklistener.ComingSoonClickListener;
import de.cycodly.worldsystem.gui.clicklistener.CommandExecutorClickListener;
import de.cycodly.worldsystem.gui.PlayerOptionStatus;
import de.cycodly.worldsystem.util.PlayerWrapper;
import de.cycodly.worldsystem.wrapper.WorldPlayer;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerOptionsGUI extends OrcInventory {

    private final static String path = "options.player.";

    public PlayerOptionsGUI(Player loader, String otherPlayer, UUID other) {
        super(GuiConfig.getTitle(GuiConfig.getConfig(), "options.player").replace("%player", otherPlayer), GuiConfig.getRows("options.player"), GuiConfig.isFill("options.player"));
        WorldPlayer wp = new WorldPlayer(PlayerWrapper.getOfflinePlayer(other), loader.getWorld().getName());
        loadItem("build", "/ws togglebuild " + otherPlayer, new PlayerOptionStatus(wp, "build"));
        loadItem("gamemode", "/ws togglegm " + otherPlayer, new PlayerOptionStatus(wp, "gamemode"));
        loadItem("teleport", "/ws toggletp " + otherPlayer, new PlayerOptionStatus(wp, "teleport"));
        loadItem("worldedit", "/ws togglewe " + otherPlayer, new PlayerOptionStatus(wp, "worldedit"));
        loadItem("time");
        loadItem("addmember");
        loadItem("delmember");
        loadItem("setpermissions");
        loadItem("administrateworld");

        if (GuiConfig.isEnabled(path + "back")) {
            OrcItem back = OrcItem.back.clone();
            back.setOnClick((p, inv, i) -> {
                p.closeInventory();
                PlayersPageGUI.openGUI(p);
            });
            addItem(GuiConfig.getSlot(path + "back"), back);
        }
    }

    public void loadItem(String subpath, String message, DependListener depend) {
        if (!GuiConfig.isEnabled(path + subpath))
            return;
        OrcItem item = GuiConfig.getItem(path + subpath);
        if (item != null) {
            if (message == null) {
                item.setOnClick(new ComingSoonClickListener());
            } else {
                item.setOnClick(new CommandExecutorClickListener(message));
            }
            addItem(GuiConfig.getSlot(path + subpath), item);
            if (depend == null) {
                addItem(GuiConfig.getState(path + subpath), OrcItem.coming_soon.clone());
            } else {
                addItem(GuiConfig.getState(path + subpath), OrcItem.disabled.clone().setDepend(depend));
            }
        }
    }

    public void loadItem(String subpath, String message) {
        loadItem(subpath, message, null);
    }

    public void loadItem(String subpath) {
        loadItem(subpath, null);
    }
}
