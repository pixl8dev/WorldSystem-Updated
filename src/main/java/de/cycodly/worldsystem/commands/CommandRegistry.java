package de.cycodly.worldsystem.commands;

import de.cycodly.worldsystem.commands.WSCommands;
import de.cycodly.worldsystem.commands.WorldAdministrateCommand;
import de.cycodly.worldsystem.commands.WorldSettingsCommands;
import de.cycodly.worldsystem.util.WorldUtils;
import de.cycodly.worldsystem.wrapper.WorldTemplateProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CommandRegistry implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            WSCommands ws = new WSCommands();
            return ws.mainCommand(sender, command, label, args);
        } else {
            WSCommands ws = new WSCommands();
            WorldAdministrateCommand admin = new WorldAdministrateCommand();
            WorldSettingsCommands settings = new WorldSettingsCommands();
            switch (args[0]) {
                //WSCommands
                case "get":
                        return ws.getCommand(sender, command, label, args);
                case "gui":
                        return ws.guiCommand(sender, command, label, args);
                /*case "confirm":
                    if (sender.hasPermission("ws.confirm")) {
                        return ws.confirmCommand(sender, command, label, args);
                    } else {
                        return false;
                    }*/
                case "home":
                    return ws.homeCommand(sender, command, label, args);
                case "info":
                    return ws.infoComannd(sender, command, label, args);
                case "leave":
                    return ws.leaveCommand(sender, command, label, args);
                case "tp":
                        return ws.tpCommand(sender, command, label, args);
                //Admin Command
                case "day":
                    return admin.setTime(sender,0);
                case "night":
                    return admin.setTime(sender,14000);
                case "time":
                    return admin.setTime(sender, args[1]);
                case "rain":
                case "storm":
                    return admin.setStorm(sender,true);
                case "sun":
                    return admin.setStorm(sender, false);
                case "delmember":
                    return admin.delMemberCommand(sender, command, label, args);
                case "delete":
                    if (sender.hasPermission("ws.delete")) {
                        return admin.deleteCommand(sender, command, label, args);
                    } else {
                        return false;
                    }
                case "addmember":
                    return admin.addMemberCommand(sender, command, label, args);
                case "toggletp":
                    return admin.toggleTeleportCommand(sender, command, label, args);
                case "togglegm":
                    return admin.toggleGamemodeCommand(sender, command, label, args);
                case "togglewe":
                    return admin.toggleWorldeditCommand(sender, command, label, args);
                case "togglebuild":
                    return admin.toggleBuildCommand(sender, command, label, args);
                //World Settings Command
                case "reset":
                    return settings.resetCommand(sender, command, label, args);
                case "sethome":
                    sender.sendMessage("Disabled For Major Error and Rework");
                    return false;
                            /*
                    if (sender.hasPermission("ws.sethome")) {
                        return settings.setHomeCommand(sender, command, label, args);
                    } else {
                        return false;
                    }*/
                case "tnt":
                    return settings.tntCommand(sender, command, label, args);
                case "fire":
                    return settings.fireCommand(sender, command, label, args);
                case "reload":
                    if(!sender.isOp()){
                        sender.sendMessage("Reloading Settings!");
                        WorldTemplateProvider.getInstance().reload();
                        WorldUtils.reloadWorldSettings();
                        return true;
                    }
                default:
                    //default command is better than the other one
                    return ws.mainCommand(sender, command, label, args);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subCommands = new ArrayList<>(Arrays.asList("get", "home", "sethome", "gui", "tp", "addmember", "delmember", "leave", "tnt", "fire", "togglegm", "togglebuild", "toggletp", "togglewe", "info", "reset"));
        if (sender.hasPermission("ws.delete")) subCommands.add("delete");
        List<String> playerCompletions = Arrays.asList("addmember", "delmember", "tp","togglegm", "togglebuild", "toggletp", "togglewe", "delete");
        List<String> completions = new ArrayList<>();
        List<String> playerNames = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            playerNames.add(p.getName());
        }

        if (args.length == 1) {
            for(String s : subCommands) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
        }

        if (args.length == 2 && playerCompletions.contains(args[0].toLowerCase())) {
            for(String s : playerNames) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase())) completions.add(s);
            }
        }

        Collections.sort(completions);

        return completions;
    }
}
