package de.cycodly.worldsystem;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.cycodly.worldsystem.commands.CommandRegistry;
import de.cycodly.worldsystem.config.DependenceConfig;
import de.cycodly.worldsystem.config.GuiConfig;
import de.cycodly.worldsystem.config.MessageConfig;
import de.cycodly.worldsystem.config.PluginConfig;
import de.cycodly.worldsystem.config.SettingsConfig;
import de.cycodly.worldsystem.listener.BlockListener;
import de.cycodly.worldsystem.listener.CommandListener;
import de.cycodly.worldsystem.listener.PlayerListener;
import de.cycodly.worldsystem.listener.WorldEditListener;
import de.cycodly.worldsystem.listener.WorldInitSkipSpawn;
import de.cycodly.worldsystem.util.PapiExtension;
import de.cycodly.worldsystem.util.PlayerPositions;
import de.cycodly.worldsystem.util.VersionUtil;
import de.cycodly.worldsystem.database.DataProvider;
import de.cycodly.worldsystem.wrapper.ICreatorAdapter;
import de.cycodly.worldsystem.wrapper.SystemWorld;
import de.cycodly.worldsystem.wrapper.AsyncCreatorAdapter;

/**
 * @author Butzlabben
 * @author Jubeki
 * @author CrazyCloudCraft
 * @version 2.2.0.1
 * @since 10.07.2017
 */
public class WorldSystem extends JavaPlugin {
    private static boolean is1_13Plus = false;
    final private String version = this.getDescription().getVersion();
    private ICreatorAdapter creator;
    

    public static void createConfigs() {
        File folder = getInstance().getDataFolder();
        File dir = new File(folder + "/worldsources");
        File config = new File(folder, "config.yml");
        File dconfig = new File(folder, "dependence.yml");
        File languages = new File(folder + "/languages");
        File gui = new File(folder, "gui.yml");
        String[] langfile = {"en","de","hu","nl","pl","es","ru","fi","ja","zh","fr"};

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!languages.exists()) {
            languages.mkdirs();
        }

        PluginConfig.checkConfig(config);

        for (String lang : langfile) {
            MessageConfig.checkConfig(new File(languages, lang+".yml"));
        }
        /*MessageConfig.checkConfig(new File(languages, "en.yml"));
        MessageConfig.checkConfig(new File(languages, "de.yml"));
        MessageConfig.checkConfig(new File(languages, "hu.yml"));
        MessageConfig.checkConfig(new File(languages, "nl.yml"));
        MessageConfig.checkConfig(new File(languages, "pl.yml"));
        MessageConfig.checkConfig(new File(languages, "es.yml"));
        MessageConfig.checkConfig(new File(languages, "ru.yml"));
        MessageConfig.checkConfig(new File(languages, "fi.yml"));
        MessageConfig.checkConfig(new File(languages, "ja.yml"));
        MessageConfig.checkConfig(new File(languages, "zh.yml"));
        MessageConfig.checkConfig(new File(languages, "fr.yml"));*/

        MessageConfig.checkConfig(new File(languages, PluginConfig.getLanguage() + ".yml"));

        if (!dconfig.exists()) {
            try {
                dconfig.createNewFile();
            } catch (IOException e) {
                WorldSystem.logger().log(Level.SEVERE, "Wasn't able to create DependenceConfig");
                e.printStackTrace();
            }
            new DependenceConfig();
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
        SettingsConfig.checkConfig();

        File worlddir = new File(cfg.getString("worldfolder"));
        if (!worlddir.exists()) {
            worlddir.mkdirs();
        }

        GuiConfig.checkConfig(gui);
    }

    public static WorldSystem getInstance() {
        return JavaPlugin.getPlugin(WorldSystem.class);
    }

    public static Logger logger() {
        return WorldSystem.getPlugin(WorldSystem.class).getLogger();
    }

    @Override
    public void onEnable() {

        getCommand("ws").setExecutor(new CommandRegistry());
        getCommand("ws").setTabCompleter(new CommandRegistry());

        // Set right version
        if (VersionUtil.getVersion() >= 13)
            is1_13Plus = true;

        createConfigs();

        // Establish database connection
        DataProvider.instance.util.connect();

        // Check if tables exist and create them if necessary.
        PlayerPositions.instance.checkTables();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new BlockListener(), this);
        pm.registerEvents(new CommandListener(), this);
        pm.registerEvents(new WorldInitSkipSpawn(), this);
        if (pm.getPlugin("WorldEdit") != null)
            pm.registerEvents(new WorldEditListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldCheckerRunnable(), 20 * 5,
                20 * PluginConfig.getLagCheckPeriod());

        if (PluginConfig.useGC()) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GCRunnable(), 20 * 5,
                    20 * PluginConfig.getGCPeriod());
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World w : Bukkit.getWorlds()) {
                SystemWorld.tryUnloadLater(w);
            }
        }, 20 * 60 * 2, 20 * 60 * 2);

        if (Bukkit.getPluginManager().getPlugin("Chunky") != null
                && PluginConfig.loadWorldsASync()
                && !is1_13Plus) {

            creator = new AsyncCreatorAdapter();
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "Found Chunky! Worlds now will be created asynchronously");
        } else {
            creator = (c, sw, r) -> {
                Bukkit.getWorlds().add(c.createWorld());
                if (sw != null)
                    sw.setCreating(false);
                r.run();
            };
        }

        // Remove old worlds option #28
        if (PluginConfig.shouldDelete()) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix()
                    + "Searching for old worlds to delete if not loaded for " + PluginConfig.deleteAfter() + " days");
            DependenceConfig.checkWorlds();
        }

        Bukkit.getConsoleSender()
                .sendMessage(PluginConfig.getPrefix() + "Successfully enabled WorldSystem v" + version);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    new PapiExtension().register();
                    Bukkit.getConsoleSender()
                            .sendMessage(PluginConfig.getPrefix() + "Successfully enabled placeholders");
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            PluginConfig.getPrefix() + "PlaceholderAPI not found. No placeholders registered");
                }
            }
        }.runTaskLater(this, 20L); // Delay of 20 ticks (1 second)
    }

    @Override
    public void onDisable() {
        for (World w : Bukkit.getWorlds()) {
            SystemWorld sw = SystemWorld.getSystemWorld(w.getName());
            if (sw != null && sw.isLoaded()) {
                sw.directUnload(w);
            }
        }

        // Close database connection
        DataProvider.instance.util.close();

        Bukkit.getConsoleSender()
                .sendMessage(PluginConfig.getPrefix() + "Successfully disabled WorldSystem v" + version);
    }

    public ICreatorAdapter getAdapter() {
        return creator;
    }
}
