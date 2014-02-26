package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class CommandManager {

        private static final HashMap<String, Command> commands = new HashMap<>();
        private static final FileConfiguration config = Main.getInstance().getConfig();
        private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        private CommandManager() {
        }

        public static void init() {
                commands.clear();
                for (String command : Main.getInstance().getConfig().getConfigurationSection("Commands").getKeys(false)) {
                        Command cmd = new Command(command);
                        commands.put(command.toLowerCase(), cmd);
                }
        }

        public static Command getCommand(String name) {
                return commands.get(name.toLowerCase());
        }

        public static boolean isRegistered(String command) {              
                return commands.containsKey(command.toLowerCase());
        }

        public static String replaceValues(String message, Player player, String[] args) {
                message = message.replaceAll("%sender%", player.getName())
                                .replaceAll("%realtime%", format.format(new Date()))
                                .replaceAll("%onlineplayers%", String.valueOf(Bukkit.getOnlinePlayers().length))
                                .replaceAll("%world%", player.getWorld().getName())
                                .replace("%cmd%", message);
                String names = "";
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (names.isEmpty()) {
                                names = onlinePlayer.getName();
                        } else {
                                names += "\n" + onlinePlayer.getName();
                        }
                }
                message = message.replace("%online", names);
                int length = args.length;
                for (int i = 1; i < length; i++) {
                        String arg = args[i];
                        message = message.replaceAll("%arg" + i, arg);
                }
                message = replaceColros(message);
                return message;
        }

        public static String getNoPermissionMessage(Player player) {
                String msg = config.getString("NoPermissionMessage");
                msg = msg.replaceAll("%player%", player.getName());
                msg = replaceColros(msg);
                return msg;
        }

        public static boolean isBlocked(String command, String world) {
                List<String> blocked = config.getStringList("BlockedWorlds." + world);
                return blocked != null && blocked.contains(command);
        }
        
        public static String getCommandIsBlockedMessage(Player player, String world, String command) {
                String msg = config.getString("CommandIsBlocked");
                msg = msg.replaceAll("%player%", player.getName());
                msg = msg.replaceAll("%cmd%", command);
                msg = msg.replaceAll("%world%", world);
                msg = replaceColros(msg);
                return msg;
        }
        
        public static String replaceColros(String s) {
                return s.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
        }

}
