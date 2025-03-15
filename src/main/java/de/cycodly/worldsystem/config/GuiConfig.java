package de.cycodly.worldsystem.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.cycodly.worldsystem.guicreate.OrcItem;
import de.cycodly.worldsystem.WorldSystem;
import de.cycodly.worldsystem.util.VersionUtil;

public class GuiConfig {

    private static File file;

    private GuiConfig() {
    }

    public static void checkConfig(File f) {
        file = f;
        if (!file.exists()) {
            try {
                InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("gui.yml");
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                WorldSystem.logger().log(Level.SEVERE,"Wasn't able to create Config");
                e.printStackTrace();
            }
        }
        OrcItem.enabled = getEnabled();
        OrcItem.disabled = getDisabled();
        OrcItem.coming_soon = getComingSoon();
        OrcItem.back = getBack();
        OrcItem.fill = getFill();
    }

    public static YamlConfiguration getConfig() {
        try {
            return YamlConfiguration
                    .loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getSlot(String path) {
        YamlConfiguration getConf = getConfig();
        return (getConf.getInt(path + ".slot.row") - 1) * 9 + getConf.getInt(path + ".slot.col") - 1;
    }

    public static int getState(String path) {
        YamlConfiguration getConf = getConfig();
        return (getConf.getInt(path + ".state.row") - 1) * 9 + getConf.getInt(path + ".state.col") - 1;
    }

    public static boolean isEnabled(String path) {
        return getConfig().getBoolean(path + ".enabled", true);
    }

    public static int getRows(String path) {
        return getConfig().getInt(path + ".rows", 1);
    }

    public static String getDisplay(FileConfiguration getConf, String path) {
        return ChatColor.translateAlternateColorCodes('&', getConf.getString(path + ".display"));
    }

    public static ArrayList<String> getLore(FileConfiguration getConf, String path) {
        List<String> list = getConf.getStringList(path + ".lore");
        ArrayList<String> colored = new ArrayList<>(list.size());
        for (String s : list) {
            colored.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return colored;
    }

    public static String getTitle(FileConfiguration getConf, String path) {
        return getConf.getString(path + ".title");
    }

    public static Material getMaterial(FileConfiguration getConf, String path) {
        try {
            return Material.valueOf(getConf.getString(path + ".material").toUpperCase());
        } catch (IllegalArgumentException ex) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cUnknown material: " + path);
            return null;
        }
    }

    public static OrcItem getItem(String path) {
        YamlConfiguration getConf = getConfig();
        try {
            return new OrcItem(getMaterial(getConf, path), getDisplay(getConf, path), getLore(getConf, path));
        } catch (Exception ignored) {
        }
        return OrcItem.error.clone().setDisplay("§c" + path);
    }

    public static OrcItem getEnabled() {
        return getItem("options.enabled");
    }

    public static OrcItem getDisabled() {
        return getItem("options.disabled");
    }

    public static OrcItem getComingSoon() {
        return getItem("options.coming_soon");
    }

    private static OrcItem getBack() {
        return getItem("options.back");
    }

    private static OrcItem getFill() {
        return getItem("options.fill");
    }

    public static boolean isFill(String path) {
        return getConfig().getBoolean(path + ".fill");
    }

    public static Material getSkullItem() {
        return getMaterial(getConfig(), "options.players.playerhead");
    }
}
