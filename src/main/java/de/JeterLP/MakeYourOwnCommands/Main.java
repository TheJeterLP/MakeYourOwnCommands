package de.JeterLP.MakeYourOwnCommands;

import de.JeterLP.MakeYourOwnCommands.Command.CommandManager;
import de.JeterLP.MakeYourOwnCommands.Listener.CommandListener;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
    protected void loadConfig() {
        Main.getInstance().getDataFolder().mkdirs();
        File cfgFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
        if (!cfgFile.exists()) {
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
