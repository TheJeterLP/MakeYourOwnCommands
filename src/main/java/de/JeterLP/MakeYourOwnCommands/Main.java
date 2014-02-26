package de.JeterLP.MakeYourOwnCommands;

import de.JeterLP.MakeYourOwnCommands.Events.CommandListener;
import de.JeterLP.MakeYourOwnCommands.commands.myoc;
import de.JeterLP.MakeYourOwnCommands.utils.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class Main extends JavaPlugin {

        private ConfigFile loader = null;
        private static Main INSTANCE;
        private static CommandUtils utils;

        @Override
        public void onEnable() {
                try {
                        getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") loading...");
                        INSTANCE = this;
                        utils = new CommandUtils();
                        loader = new ConfigFile();
                        loader.loadConfig();
                        CommandManager.init();
                        new AdvancedUpdater(this, 54353, "http://dev.bukkit.org/bukkit-plugins/simple-info2/", "SearchForUpdates").search();
                        new Metrics(this).start();
                        getCommand("myoc").setExecutor(new myoc());
                        getServer().getPluginManager().registerEvents(new CommandListener(), this);
                        getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") is now enabled.");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Override
        public void onDisable() {
                getLogger().info("(by JeterLP Version: " + getDescription().getVersion() + ") is now disabled.");
        }
      
        /**
         * @return Old CommandUtils
         * @deprecated Use CommandManager class
         */
        @Deprecated
        public static CommandUtils getUtils() {
                return utils;
        }

        public static Main getInstance() {
                return INSTANCE;
        }
}
