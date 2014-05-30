package de.JeterLP.MakeYourOwnCommands;

import de.JeterLP.MakeYourOwnCommands.utils.CommandUtils;
import de.JeterLP.MakeYourOwnCommands.Command.CommandManager;
import de.JeterLP.MakeYourOwnCommands.Listener.CommandListener;
import de.JeterLP.MakeYourOwnCommands.utils.*;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
//import org.mcstats.Metrics;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private YamlConfiguration cfg;

    /**
     * Loads the plugin.
     */
    @Override
    public void onEnable() {
        try {
            getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") loading...");
            INSTANCE = this;
            loadConfig();
            new AdvancedUpdater(this, 54353, "http://dev.bukkit.org/bukkit-plugins/simple-info2/", "SearchForUpdates").search();
            new Metrics(this).start();
            CommandManager.init();
            getCommand("myoc").setExecutor(new MyocCommand());
            getServer().getPluginManager().registerEvents(new CommandListener(), this);
            getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") is now enabled.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Disables the plugin.
     */
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") is now disabled.");
    }

    /**
     * @return Old CommandUtils
     * @deprecated Use CommandManager class
     */
    @Deprecated
    public static CommandUtils getUtils() {
        return new CommandUtils();
    }

    /**
     * You can access the Main class using this method.
     *
     * @return INSTANCE: The current instance of the Main class.
     */
    public static Main getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        Main.getInstance().getDataFolder().mkdirs();
        File cfgFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
        if (cfgFile.exists()) {
            if (cfg.getInt("Version") != 1) {
                cfgFile.renameTo(new File(Main.getInstance().getDataFolder(), "config_old.yml"));
                saveResource("config.yml", false);
            }
        } else {
            saveResource("config.yml", false);
        }
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    /**
     * Gets the configuration
     *
     * @return cfg
     */
    @Override
    public YamlConfiguration getConfig() {
        return cfg;
    }

}
