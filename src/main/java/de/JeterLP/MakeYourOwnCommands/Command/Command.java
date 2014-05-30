package de.JeterLP.MakeYourOwnCommands.Command;

import de.JeterLP.MakeYourOwnCommands.Events.PlayerRunMyocCommandEvent;
import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.utils.LocationHelper;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author TheJeterLP
 */
public class Command {

    private CommandType type;
    private String command;
    private String permission, sendTo;
    private List<String> messages;
    private LocationHelper TPLOC = null;
    private String EXECUTE = null;
    private ItemStack ITEM = null;

    private final boolean valid;

    protected Command(String commandName) {
        Main.getInstance().getLogger().info("Loading command " + commandName);
        FileConfiguration cfg = Main.getInstance().getConfig();
        if (CommandType.isValid(cfg.getString("Commands." + commandName + ".mode"))) {
            type = CommandType.getByName(cfg.getString("Commands." + commandName + ".mode"));
            command = commandName;
            permission = cfg.getString("Commands." + commandName + ".permission");
            sendTo = cfg.getString("Commands." + commandName + ".sendTo");
            messages = cfg.getStringList("Commands." + commandName + ".messages");
            if (type == CommandType.ALIAS) {
                EXECUTE = cfg.getString("Commands." + commandName + ".execute");
            } else if (type == CommandType.TELEPORT) {
                String world = cfg.getString("Commands." + commandName + ".world");
                int x = cfg.getInt("Commands." + commandName + ".x");
                int y = cfg.getInt("Commands." + commandName + ".y");
                int z = cfg.getInt("Commands." + commandName + ".z");
                float yaw = Float.valueOf(cfg.getString("Commands." + commandName + ".yaw"));
                float pitch = Float.valueOf(cfg.getString("Commands." + commandName + ".pitch"));
                int delay = cfg.getInt("Commands." + commandName + ".delay");
                TPLOC = new LocationHelper(delay, x, y, z, yaw, pitch, world);
            } else if (type == CommandType.ITEM) {
                Material mat = null;
                String item = cfg.getString("Commands." + commandName + ".Item.Material");
                try {
                    mat = Material.getMaterial(Integer.valueOf(item));
                } catch (NumberFormatException ex) {
                    mat = Material.valueOf(item);
                }
                int amount = cfg.getInt("Commands." + commandName + ".Item.Amount");
                ITEM = new ItemStack(mat, amount);
            }
            valid = true;
        } else {
            Main.getInstance().getLogger().severe(commandName + " does not has a valid CommandType.");
            valid = false;
        }
    }

    /**
     * @return type: The {@link de.JeterLP.MakeYourOwnCommands.Command.CommandType} of the Command. Returns null if the command is not valid.
     */
    public CommandType getType() {
        if (!valid) return null;
        return type;
    }

    /**
     * @return ITEM: The {@link org.bukkit.inventory.ItemStack} for this command.
     * Returns null if the command is not valid, or the type is not ITEM.
     */
    public ItemStack getItem() {
        if (!valid || type != CommandType.ITEM) return null;
        return ITEM;
    }

    /**
     * @return valid: If the command was successfully initialized.
     */
    @Nonnull
    public boolean isValid() {
        return valid;
    }

    /**
     * @return command: The command which is executed.
     * Returns null if the command is not valid.
     */
    public String getCommand() {
        if (!valid) return null;
        return command;
    }

    /**
     * @return permission: The permission needed to execute the command.
     * Returns null if the command is not valid.
     */
    public String getPermission() {
        if (!valid) return null;
        return permission;
    }

    /**
     * @return sendTo: The SendTo for this command.
     * Returns null if the command is not valid.
     */
    public String getSendTO() {
        if (!valid) return null;
        return sendTo;
    }

    /**
     * @return messages: The messages which should be sent to the executing player.
     * Returns null if the command is not valid.
     */
    public List<String> getMessages() {
        if (!valid) return null;
        return Collections.unmodifiableList(messages);
    }

    /**
     * @return EXECUTE: Returns the command which should be executed. if the command is an alias.
     * Returns null if the command is not valid, or the type is not ALIAS.
     */
    public String getExecute() {
        if (!valid || type != CommandType.ALIAS) return null;
        return EXECUTE;
    }

    /**
     * @return TPLOC: The {@link de.JeterLP.MakeYourOwnCommands.utils.LocationHelper} for the command if its a TELEPORT command.
     * Returns null if the command is not valid, or the type is not TELEPORT.
     */
    public LocationHelper getLocation() {
        if (!valid || type != CommandType.TELEPORT) return null;
        return TPLOC;
    }

    /**
     * @param player: The player who is executing the command.
     * @param args: Used for the messages.
     * @param fullCMD: Used to log the command.
     * @param permNeeded: Should be checked if the player has the needed permission?
     * @param checkBlocked: Should be checked if the command is blocked in the world?
     * Executes the command.
     * Does nothing if the command is not valid.
     * Executes a {@link de.JeterLP.MakeYourOwnCommands.Events.PlayerRunMyocCommandEvent}.
     */
    public void execute(Player player, String[] args, String fullCMD, boolean permNeeded, boolean checkBlocked) {
        if (!valid) return;
        String noperm = CommandManager.getNoPermissionMessage(player);

        if (permNeeded) {
            if (!player.hasPermission(permission)) {
                player.sendMessage(noperm);
                return;
            }
        }

        if (checkBlocked) {
            if (CommandManager.isBlocked(command, player.getWorld().getName())) {
                String blocked = CommandManager.getCommandIsBlockedMessage(player, player.getWorld().getName(), command);
                player.sendMessage(blocked);
                return;
            }
        }

        PlayerRunMyocCommandEvent event = new PlayerRunMyocCommandEvent(player, this);
        Main.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        Bukkit.getLogger().info(player.getName() + " issued server command: " + fullCMD);

        switch (type) {
            case TELEPORT:
                TPLOC.teleport(player);
                sendMessages(args, player);
                break;
            case ALIAS:
                player.performCommand(EXECUTE);
                sendMessages(args, player);
                break;
            case MESSAGE:
                sendMessages(args, player);
                break;
            case ITEM:
                player.getInventory().addItem(ITEM);
                sendMessages(args, player);
                break;
        }

    }

    /**
     * @param args: The arguments given with the command.
     * @param player: The Player who is executing the command.
     * Sends all messages from the command to the executing Player.
     */
    public void sendMessages(String[] args, Player player) {
        switch (sendTo) {
            case "sender":
                for (String s : messages) {
                    String msg = CommandManager.replaceValues(s, player, args);
                    player.sendMessage(msg);
                }
                break;
            case "online":
                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                    for (String s : messages) {
                        String msg = CommandManager.replaceValues(s, player, args);
                        p.sendMessage(msg);
                    }
                }
                break;
            case "op":
                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                    if (!p.isOp()) {
                        continue;
                    }
                    for (String s : messages) {
                        String msg = CommandManager.replaceValues(s, player, args);
                        p.sendMessage(msg);
                    }
                }
                break;
            case "permission":
                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                    if (!p.hasPermission(permission)) {
                        continue;
                    }
                    for (String s : messages) {
                        String msg = CommandManager.replaceValues(s, player, args);
                        p.sendMessage(msg);
                    }
                }
                break;
            default:
                player.sendMessage("§c[ERROR] §7Wrong sendTo! You can use: online, sender, op, permission");
                break;

        }
    }

}
