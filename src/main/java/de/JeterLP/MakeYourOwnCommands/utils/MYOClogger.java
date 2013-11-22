package de.JeterLP.MakeYourOwnCommands.utils;

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

        UPDATE("[UPDATE-SYSTEM]"),
        INFO(""),
        ERROR("");
        private final String prefix;
        
        Type(String s) {
            prefix = s;
        }
        
        boolean hasPrefix() {
            return !prefix.isEmpty();
        }
    }

    public static void log(Type type, String message) {
        if (type.equals(Type.ERROR)) {
            logger.log(Level.SEVERE, PREFIX + "{0}", message);
        } else if (type.equals(Type.UPDATE)) {
            logger.log(Level.INFO, PREFIX + type.prefix + "{0}", message);
        } else {
            logger.log(Level.INFO, PREFIX + "{0}", message);
        }
    }
}
