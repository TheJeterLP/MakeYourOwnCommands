package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author JeterLP
 */
public class ConfigFile {

    private final Main main;

    public ConfigFile(Main main) {
        this.main = main;
    }

    /**
     * This method loads the config of this plugin
     */
    public void loadConfig() {
        File cfg = new File("plugins/MakeYourOwnCommands/config.yml");
        main.getDataFolder().mkdirs();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(cfg);
        int version = config.getInt("Version");
        if (cfg.exists()) {
            if (version != 1) {
                if (new File("plugins/MakeYourOwnCommands/config_old.yml").exists()) {
                    File del = new File("plugins/MakeYourOwnCommands/config_old.yml");
                    del.delete();
                }
                cfg.renameTo(new File("plugins/MakeYourOwnCommands/config_old.yml"));
                System.out.println("The config has changed. You find your old config in /plugins/MakeYourOwnCommands/config_old.yml");
                generateConfig();
            }
            main.getConfig().options().copyDefaults(true);
        } else {
            generateConfig();
            main.getConfig().options().copyDefaults(true);
        }
    }

    private void generateConfig() {
        MYOClogger.log(MYOClogger.Type.INFO, "Generating a new config for you...");
        main.saveResource("config.yml", false);
        MYOClogger.log(MYOClogger.Type.INFO, "Finished!");
    }

    public FileConfiguration getConfig() {
        return main.getConfig();
    }
}
