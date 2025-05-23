package de.cycodly.worldsystem.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.cycodly.worldsystem.WorldSystem;
import de.cycodly.worldsystem.util.PlayerPositions;
import net.md_5.bungee.api.ChatColor;

public class PluginConfig {

    private final static GameMode[] GAME_MODES = new GameMode[]{GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE, GameMode.SPECTATOR};
    private static File FILE;

    private PluginConfig() {}

    public static void checkConfig(File f) {
        FILE = f;
        if (FILE.exists()) {
            YamlConfiguration cfg = getConfig();
            if (!(cfg.isString("worldfolder") && cfg.isInt("unloadingtime")
                    && cfg.isBoolean("survival") && cfg.isString("language") && cfg.isString("prefix")
                    && cfg.isInt("request_expires") && cfg.isBoolean("need_confirm")
                    && cfg.isBoolean("contact_authserver") && cfg.isBoolean("spawn_teleportation")
                    && cfg.isInt("delete_after") && cfg.isBoolean("worldtemplates.multi_choose")
                    && cfg.isString("worldtemplates.default") && cfg.isBoolean("load_worlds_async") &&

                    // Database stuff
                    cfg.isString("database.type") && cfg.isString("database.worlds_table_name") && cfg.isString("database.players_table_name")
                    && cfg.isString("database.mysql_settings.host") && cfg.isInt("database.mysql_settings.port")
                    && cfg.isString("database.mysql_settings.username") && cfg.isString("database.mysql_settings.password")
                    && cfg.isString("database.mysql_settings.database") && cfg.isString("database.sqlite_settings.file") &&

                    cfg.isInt("lagsystem.period_in_seconds") && cfg.isInt("lagsystem.entities_per_world")
                    && cfg.isBoolean("lagsystem.garbagecollector.use")
                    && cfg.isInt("lagsystem.garbagecollector.period_in_minutes") &&

                    cfg.isString("spawn.spawnpoint.world") && cfg.isInt("spawn.gamemode")
                    && cfg.isBoolean("spawn.spawnpoint.use_last_location")
                    && (cfg.isDouble("spawn.spawnpoint.x") || cfg.isInt("spawn.spawnpoint.x"))
                    && (cfg.isDouble("spawn.spawnpoint.y") || cfg.isInt("spawn.spawnpoint.y"))
                    && (cfg.isDouble("spawn.spawnpoint.z") || cfg.isInt("spawn.spawnpoint.z"))
                    && (cfg.isDouble("spawn.spawnpoint.yaw") || cfg.isInt("spawn.spawnpoint.yaw"))
                    && (cfg.isDouble("spawn.spawnpoint.pitch") || cfg.isInt("spawn.spawnpoint.pitch")) &&

                    cfg.isBoolean("worldspawn.use") && cfg.isBoolean("worldspawn.use_last_location")
                    && (cfg.isDouble("worldspawn.spawnpoint.x") || cfg.isInt("worldspawn.spawnpoint.x"))
                    && (cfg.isDouble("worldspawn.spawnpoint.y") || cfg.isInt("worldspawn.spawnpoint.y"))
                    && (cfg.isDouble("worldspawn.spawnpoint.z") || cfg.isInt("worldspawn.spawnpoint.z"))
                    && (cfg.isDouble("worldspawn.spawnpoint.yaw") || cfg.isInt("worldspawn.spawnpoint.yaw"))
                    && (cfg.isDouble("worldspawn.spawnpoint.pitch") || cfg.isInt("worldspawn.spawnpoint.pitch")))) {
                try {
                    Files.copy(FILE.toPath(),
                            new File(FILE.getParentFile(), "config-broken-"
                                    + new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date()) + ".yml").toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(FILE.toPath());
                    WorldSystem.logger().log(Level.SEVERE,"[WorldSystem] Config is broken, creating a new one!");
                    checkConfig(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                // Create parent directories if they don't exist
                if (!FILE.getParentFile().exists()) {
                    FILE.getParentFile().mkdirs();
                }
                // Try to get the config from resources
                InputStream in = JavaPlugin.getPlugin(WorldSystem.class).getResource("config.yml");
                if (in != null) {
                    // Copy from resources
                    Files.copy(in, FILE.toPath());
                    in.close();
                } else {
                    // Create a new config with default values if resource not found
                    YamlConfiguration config = new YamlConfiguration();
                    config.set("prefix", "&8[&3WorldSystem&8] &6");
                    config.set("command_prefix", "realms");
                    config.set("worldfolder", "plugins/WorldSystem/Worlds");
                    config.set("language", "en");
                    config.set("need_confirm", true);
                    config.set("contact_authserver", true);
                    config.set("spawn.gamemode", 2);
                    config.set("spawn.spawnpoint.use_last_location", false);
                    config.set("spawn.spawnpoint.world", "world");
                    config.set("spawn.spawnpoint.x", 0);
                    config.set("spawn.spawnpoint.y", 64);
                    config.set("spawn.spawnpoint.z", 0);
                    config.set("spawn.spawnpoint.yaw", 0);
                    config.set("spawn.spawnpoint.pitch", 0);
                    config.set("request_expires", 20);
                    config.set("load_worlds_async", true);
                    config.set("unloadingtime", 20);
                    config.set("delete_after", -1);
                    config.set("lagsystem.period_in_seconds", 10);
                    config.set("lagsystem.entities_per_world", 350);
                    config.set("lagsystem.garbagecollector.use", false);
                    config.set("lagsystem.garbagecollector.period_in_minutes", 5);
                    config.set("database.type", "sqlite");
                    config.set("database.worlds_table_name", "worlds_positions");
                    config.set("database.players_table_name", "player_positions");
                    config.set("database.players_uuids", "players_uuids");
                    config.set("database.mysql_settings.host", "127.0.0.1");
                    config.set("database.mysql_settings.port", 3306);
                    config.set("database.mysql_settings.username", "root");
                    config.set("database.mysql_settings.password", "YOUR_PASSWORD_HERE");
                    config.set("database.mysql_settings.database", "database");
                    config.set("database.sqlite_settings.file", "plugins/WorldSystem/repository.db");
                    config.set("survival", true);
                    config.set("spawn_teleportation", true);
                    config.set("worldspawn.use_last_location", true);
                    config.set("worldspawn.use", false);
                    config.set("worldspawn.spawnpoint.x", 0);
                    config.set("worldspawn.spawnpoint.y", 20);
                    config.set("worldspawn.spawnpoint.z", 0);
                    config.set("worldspawn.spawnpoint.yaw", 0);
                    config.set("worldspawn.spawnpoint.pitch", 0);
                    config.set("worldtemplates.multi_choose", false);
                    config.set("worldtemplates.default", "template_default");
                    config.save(FILE);
                }
            } catch (IOException e) {
                WorldSystem.logger().log(Level.SEVERE,"Wasn't able to create Config", e);
            }
        }

        // Should fix #2
        if (getSpawn(null).getWorld() == null) {
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cA lobby/hub spawn is missing. If this is not the first launch, add a spawn in config.yml");
        }
    }

    public static YamlConfiguration getConfig() {
        try {
            return YamlConfiguration
                    .loadConfiguration(new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot access config file");
    }

    public static int getGCPeriod() {
        return getConfig().getInt("lagsystem.garbagecollector.period_in_minutes", 5);
    }

    public static boolean useGC() {
        return getConfig().getBoolean("lagsystem.garbagecollector.use", false);
    }

    public static int getEntitysPerWorld() {
        return getConfig().getInt("lagsystem.entities_per_world", 350);
    }

    public static int getLagCheckPeriod() {
        return getConfig().getInt("lagsystem.period_in_seconds", 10);
    }

    public static boolean useWorldSpawn() {
        return getConfig().getBoolean("worldspawn.use", true);
    }

    public static boolean isSurvival() {
        return getConfig().getBoolean("survival", false);
    }

    public static int getUnloadingTime() {
        return getConfig().getInt("unloadingtime", 20);
    }

    public static GameMode getSpawnGamemode() {
        return GAME_MODES[getConfig().getInt("spawn.gamemode", 2)];
    }

    public static String getWorlddir() {
        return getConfig().getString("worldfolder", "plugins/WorldSystem/Worlds") + "/";
    }

    public static boolean isMultiChoose() {
        return getConfig().getBoolean("worldtemplates.multi_choose", false);
    }

    public static String getDefaultWorldTemplate() {
        return getConfig().getString("worldtemplates.default", "");
    }

    public static String getLanguage() {
        return getConfig().getString("language", "en");
    }

    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "§8[§3WorldSystem§8] §6"));
    }

    public static String getCommandPrefix() {
        return getConfig().getString("cmd_prefix", "ws");
    }

    public static Location getWorldSpawn(World w) {
        return getLocation(getConfig(), "worldspawn.spawnpoint", w);
    }

    public static Location getSpawn(Player player) {
        YamlConfiguration cfg = getConfig();
        Location location = getLocation(cfg, "spawn.spawnpoint", Bukkit.getWorld(cfg.getString("spawn.spawnpoint.world", "world")));
        return PlayerPositions.instance.injectPlayersLocation(player, location);
    }

    public static int getRequestExpire() {
        return getConfig().getInt("request_expires", 20);
    }

    private static Location getLocation(YamlConfiguration cfg, String path, World world) {
        return new Location(world, cfg.getDouble(path + ".x", 0), cfg.getDouble(path + ".y", 20),
                cfg.getDouble(path + ".z", 0), (float) cfg.getDouble(path + ".yaw", 0),
                (float) cfg.getDouble(path + ".pitch", 0));
    }

    /**
     * public static boolean confirmNeed() {
     * return getConfig().getBoolean("need_confirm", true);
     * }
     **/

    public static boolean contactAuth() {
        return getConfig().getBoolean("contact_authserver", true);
    }

    public static boolean spawnTeleportation() {
        return getConfig().getBoolean("spawn_teleportation", true);
    }

    public static boolean shouldDelete() {
        return getConfig().getInt("delete_after") != -1;
    }

    public static long deleteAfter() {
        return getConfig().getLong("delete_after");
    }

    public static boolean useWorldSpawnLastLocation() {
        return getConfig().getBoolean("worldspawn.use_last_location");
    }

    public static boolean useSpawnLastLocation() {
        return getConfig().getBoolean("spawn.spawnpoint.use_last_location");
    }

    public static String getWorldsTableName() {
        return getConfig().getString("database.worlds_table_name");
    }

    public static String getPlayersTableName() {
        return getConfig().getString("database.players_table_name");
    }

    public static String getUUIDTableName() {
        return getConfig().getString("database.players_uuids");
    }



    public static String getDatabaseType() {
        return getConfig().getString("database.type");
    }

    public static String getSqliteFile() {
        return getConfig().getString("database.sqlite_settings.file");
    }

    public static String getMysqlHost() {
        return getConfig().getString("database.mysql_settings.host");
    }

    public static int getMysqlPort() {
        return getConfig().getInt("database.mysql_settings.port");
    }

    public static String getMysqlUser() {
        return getConfig().getString("database.mysql_settings.username");
    }

    public static String getMysqlPassword() {
        return getConfig().getString("database.mysql_settings.password");
    }

    public static String getMysqlDatabaseName() {
        return getConfig().getString("database.mysql_settings.database");
    }

    public static boolean loadWorldsASync() {
        return getConfig().getBoolean("load_worlds_async");
    }
}
