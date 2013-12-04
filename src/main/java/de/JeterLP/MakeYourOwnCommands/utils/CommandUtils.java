package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.WrongTypeException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class CommandUtils {

        /**
         * Use Main.getUtils() for the instance
         */
        public CommandUtils() {
                config = Main.instance.getConfig();
        }

        private final FileConfiguration config;
        private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        /**
         * Gets the Type of the command
         *
         * @param command
         * @return the type of the command. (Teleport, alias, message)
         */
        public String getType(String command) {
                String type = config.getString("Commands." + command + ".mode");
                return type;
        }

        /**
         * Gets the needed permission to execute the command
         *
         * @param command
         * @return permission
         */
        public String getPermission(String command) {
                String perm = config.getString("Commands." + command + ".permission");
                return perm;
        }

        /**
         * Gets the sendto for the command. (sender, online, op, permission)
         *
         * @param command
         * @return
         */
        public String getSendTo(String command) {
                String sendto = config.getString("Commands." + command + ".sendTo");
                return sendto;
        }

        /**
         * Gets the messages from the given command
         *
         * @param command
         * @param args
         * @param p
         * @return messages
         */
        public List<String> getMessages(String command, String[] args, Player p) {
                List<String> ret = config.getStringList("Commands." + command + ".messages");
                return ret;
        }

        /**
         * Gets the Location of the command if its type is teleport
         *
         * @param command
         * @throws WrongTypeException
         * @return
         */
        public Location getTargetLocation(String command) throws WrongTypeException {
                try {
                        String world = config.getString("Commands." + command + ".world");
                        double x = config.getDouble("Commands." + command + ".x");
                        double z = config.getDouble("Commands." + command + ".z");
                        double y = config.getDouble("Commands." + command + ".y");
                        float yaw = Float.valueOf(config.getString("Commands." + command + ".yaw"));
                        float pitch = Float.valueOf(config.getString("Commands." + command + ".pitch"));
                        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        return loc;
                } catch (NullPointerException e) {
                        throw new WrongTypeException("Command is not a Teleport command.");
                }

        }

        /**
         * Gets the command to execute if its an alias.
         * @param command
         * @param player
         * @param args
         * @return
         * @throws WrongTypeException 
         */
        public String getExecute(String command, Player player, String[] args) throws WrongTypeException {
                try {
                        String cmd = config.getString("Commands." + command + ".execute");
                        cmd = replaceValues(cmd, player, args);
                        cmd = cmd.replaceFirst("/", "");
                        return cmd;
                } catch (NullPointerException e) {
                        throw new WrongTypeException("Command is not an alias.");
                }
        }

        /**
         * Gets the delay for the teleportation if its a tp command.
         * @param command
         * @return
         * @throws WrongTypeException 
         */
        public double getDelay(String command) throws WrongTypeException {
                try {
                        double ret = config.getDouble("Commands." + command + ".delay");
                        return ret;
                } catch (NullPointerException e) {
                        throw new WrongTypeException("Command is not a teleport command");
                }
        }

        /**
         * Replaces any value in the messages
         * @param message
         * @param player
         * @param args
         * @return 
         */
        public String replaceValues(String message, Player player, String[] args) {
                message = message.replaceAll("%sender%", player.getName())
                                .replaceAll("%realtime%", format.format(new Date()))
                                .replaceAll("%onlineplayers%", String.valueOf(Bukkit.getOnlinePlayers().length))
                                .replaceAll("%world%", player.getWorld().getName())
                                .replace("%cmd%", message);
                String names = "";
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (names.isEmpty() || names.equalsIgnoreCase("")) {
                                names = onlinePlayer.getName();
                        } else {
                                names = names + "\n" + onlinePlayer.getName();
                        }
                }
                message = message.replace("%online", names);
                int length = args.length;
                for (int i = 1; i < length; i++) {
                        String arg = args[i];
                        message = message.replaceAll("%", "");
                        message = message.replaceAll("arg" + i, arg);
                }
                message = message.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                return message;
        }

        /**
         * Gets the no-permission message
         * @return 
         */
        public String getNoPermissionMessage() {
                String msg = config.getString("NoPermissionMessage");
                msg = msg.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                return msg;
        }

        /**
         * Gets the no-permission message
         * @param player
         * @return 
         */
        public String getNoPermissionMessage(Player player) {
                String msg = config.getString("NoPermissionMessage");
                msg = msg.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                msg = msg.replaceAll("%player%", player.getName());
                return msg;
        }

        /**
         * Checks if a command is handled by MakeYourOwnCommands
         * @param command
         * @return 
         */
        public boolean isRegistered(String command) {
                for (String commands : config.getConfigurationSection("Commands").getKeys(false)) {
                        if (commands.equalsIgnoreCase(command)) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * Gets the command is blocked message.
         * @param player
         * @param world
         * @param command
         * @return 
         */
        public String getCommandIsBlockedMessage(Player player, String world, String command) {
                String msg = config.getString("CommandIsBlocked");
                msg = msg.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                msg = msg.replaceAll("%player%", player.getName());
                msg = msg.replaceAll("%cmd%", command);
                msg = msg.replaceAll("%world%", world);
                return msg;
        }

        /**
         * Checks if a command is blocked by MakeYourOwnCommands
         * @param command
         * @param world
         * @return 
         */
        public boolean isBlocked(String command, String world) {
                List<String> blocked = config.getStringList("BlockedWorlds." + world);
                return blocked != null && blocked.contains(command);
        }
}
