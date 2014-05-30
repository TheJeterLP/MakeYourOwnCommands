package de.JeterLP.MakeYourOwnCommands.Command;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.text.SimpleDateFormat;
import java.util.Collection;
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

    /**
     * This Class only uses static methods.
     */
    private CommandManager() {
    }

    /**
     * Initiates the CommandManager and loads all commands from the configuration.
     */
    public static void init() {
        commands.clear();
        for (String command : Main.getInstance().getConfig().getConfigurationSection("Commands").getKeys(false)) {
            Command cmd = new Command(command);
            if (cmd.isValid()) commands.put(command.toLowerCase(), cmd);
        }
    }

    /**
     * Gets all loaded commands.
     *
     * @return All loaded commands.
     */
    public static Collection<Command> getCommands() {
        return commands.values();
    }

    /**
     * Returns the given Command by the name. Has to start with /
     *
     * @param name: The CommandName
     * @return Command: The given command.
     * Returns null if the command is not existing.
     */
    public static Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    /**
     * Checks if the command is handled from MakeYourOwnCommands.
     *
     * @param command: The name of the command. Has to start with a /
     * @return true: If the command is handled by MakeYourOwnCommands
     */
    public static boolean isRegistered(String command) {
        return commands.containsKey(command.toLowerCase());
    }

    /**
     * Replaces all possible values in the message.
     *
     * @param message: Used for the values.
     * @param player: Used for the values.
     * @param args: Used for the values.
     * @return message: With all values replaced.
     */
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
        for (int i = 0; i < length; i++) {
            String arg = args[i];
            int rep = i + 1;
            message = message.replaceAll("%arg" + rep, arg);
        }
        message = replaceColors(message);
        return message;
    }

    /**
     * Returns the noPermission message specified in the config.
     *
     * @param player: Used to replace the %player% value.
     * @return msg: The noPermission message.
     */
    public static String getNoPermissionMessage(Player player) {
        String msg = config.getString("NoPermissionMessage");
        msg = msg.replaceAll("%player%", player.getName());
        msg = replaceColors(msg);
        return msg;
    }

    /**
     * Checks if the command is blocked in the given world.
     *
     * @param command: The command which should be checked.
     * @param world: Ther world where the command may be blocked:
     * @return true: If the command is blocked in the given world.
     */
    public static boolean isBlocked(String command, String world) {
        List<String> blocked = config.getStringList("BlockedWorlds." + world);
        return blocked != null && blocked.contains(command.toLowerCase()) && isRegistered(command);
    }

    /**
     * Gets the commandIsBlocked Message specified in the config.
     *
     * @param player: Used to replace the %player% value.
     * @param world: Used to replace the %world% value.
     * @param command: Used to repalce the %cmd% value.
     * @return msg: The message.
     */
    public static String getCommandIsBlockedMessage(Player player, String world, String command) {
        String msg = config.getString("CommandIsBlocked");
        msg = msg.replaceAll("%player%", player.getName());
        msg = msg.replaceAll("%cmd%", command);
        msg = msg.replaceAll("%world%", world);
        msg = replaceColors(msg);
        return msg;
    }

    /**
     * Replaces all color codes.
     *
     * @param s: The String where the colors should be replaced.
     * @return s: with all colors replaced.
     */
    public static String replaceColors(String s) {
        return s.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

}
