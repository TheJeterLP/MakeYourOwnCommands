package de.JeterLP.MakeYourOwnCommands.utils;

import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 * @deprecated use CommandManager class.
 */
@Deprecated
public class CommandUtils {

        /**
         * @param message
         * @param player
         * @param args
         * @deprecated Use CommandManager class.
         */
        @Deprecated
        public String replaceValues(String message, Player player, String[] args) {
                return CommandManager.replaceValues(message, player, args);
        }

        /**
         * @param player
         * @return String
         * @deprecated Use CommandManager class.
         */
        @Deprecated
        public String getNoPermissionMessage(Player player) {
                return CommandManager.getNoPermissionMessage(player);
        }

        /**
         * @param command
         * @return boolean
         * @deprecated Use CommandManager class.
         */
        @Deprecated
        public boolean isRegistered(String command) {
                return CommandManager.isRegistered(command);
        }

        /**
         * @param player
         * @param world
         * @param command
         * @return String
         * @deprecated Use CommandManager class.
         */
        @Deprecated
        public String getCommandIsBlockedMessage(Player player, String world, String command) {
                return CommandManager.getCommandIsBlockedMessage(player, world, command);
        }

        /**
         * @param command
         * @param world
         * @return boolean
         * @deprecated use CommandManager class.
         */
        @Deprecated
        public boolean isBlocked(String command, String world) {
                return CommandManager.isBlocked(command, world);
        }
}
