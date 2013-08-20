package de.JeterLP.MakeYourOwnCommands;

import de.JeterLP.MakeYourOwnCommands.Events.CommandListener;
import de.JeterLP.MakeYourOwnCommands.commands.myoc;
import de.JeterLP.MakeYourOwnCommands.utils.ConfigFile;
import de.JeterLP.MakeYourOwnCommands.utils.MYOClogger;
import de.JeterLP.MakeYourOwnCommands.utils.MetricsChecker;
import de.JeterLP.MakeYourOwnCommands.utils.Updatechecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JeterLP
 */
public class Main extends JavaPlugin {

    public final String prefix = "[MakeYourOwnCommands] ";
    private ConfigFile loader = null;
    private Updatechecker checker = null;
    private MetricsChecker mchecker = null;
    public FileConfiguration config;

    /**
     * This method loads the plugin
     */
    @Override
    public void onEnable() {
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP" + " Version: " + getDescription().getVersion() + ") loading...");
        loader = new ConfigFile(this);
        loader.loadConfig();
        checker = new Updatechecker(this);
        checker.checkUpdate();
        mchecker = new MetricsChecker(this);
        mchecker.checkMetrics();
        getCommand("myoc").setExecutor(new myoc(this));
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP" + " Version: " + getDescription().getVersion() + ") is now enabled.");
    }

    /**
     * This method logs that the plugin was disabled
     */
    @Override
    public void onDisable() {
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP" + " Version: " + getDescription().getVersion() + ") is now disabled.");
    }
}
