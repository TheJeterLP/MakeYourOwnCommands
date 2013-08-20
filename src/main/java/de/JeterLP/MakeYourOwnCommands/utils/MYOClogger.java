package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

/**
 * @author TheJeterLP
 */
public class MYOClogger {

    private static final String PREFIX = "[MakeYourOwnCommands] ";
    private static final Logger logger = Bukkit.getLogger();

    public static enum Type {

        INFO,
        ERROR;
    }
    
    public static void log(Type type, String message) {
        if (type.equals(Type.ERROR)) {
            logger.log(Level.SEVERE, PREFIX + "{0}", message);
        } else {
            logger.log(Level.INFO, PREFIX + "{0}", message);
        }
    }
}
