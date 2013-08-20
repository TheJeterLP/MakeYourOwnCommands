package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author JeterLP
 */
public class ConfigFile {

    private Main main;
    private File cmds = new File("plugins/MakeYourOwnCommands/CommandsUsed.txt");
    private File aliases = new File("plugins/MakeYourOwnCommands/AliasesUsed.txt");
    private File tps = new File("plugins/MakeYourOwnCommands/TeleportsUsed.txt");

    /**
     * <p>This is the constructor needed to get the Main class</p>
     *
     * @param main
     */
    public ConfigFile(Main main) {
        this.main = main;
    }

    /**
     * <p>This method loads the config of this plugin</p>
     */
    public void loadConfig() {
        if (new File("plugins/MakeYourOwnCommands/config.yml").exists()) {
            main.config = main.getConfig();
            main.config.options().copyDefaults(true);
        } else {
            main.saveDefaultConfig();
            main.config = main.getConfig();
            main.config.options().copyDefaults(true);
        }

    }

    

    public FileConfiguration getConfig() {
        return main.getConfig();
    }

}
