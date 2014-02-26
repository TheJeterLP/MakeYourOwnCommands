package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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

        private final boolean valid;

        public Command(String commandName) {
                FileConfiguration cfg = Main.getInstance().getConfig();
                if (CommandType.isValid(cfg.getString("Commands." + commandName + ".mode"))) {
                        type = CommandType.getByName(cfg.getString("Commands." + commandName + ".mode"));
                        command = commandName;
                        permission = cfg.getString("Commands." + commandName + ".permission");
                        sendTo = cfg.getString("Commands." + commandName + ".sendTo");
                        messages = cfg.getStringList("Commands." + commandName + ".mode");
                        if (type == CommandType.ALIAS) {
                                EXECUTE = cfg.getString("Commands." + commandName + ".execute");
                        } else if (type == CommandType.TELEPORT) {
                                String world = cfg.getString("Commands." + commandName + ".world");
                                int x = cfg.getInt("Commands." + commandName + ".x");
                                int y = cfg.getInt("Commands." + commandName + ".y");
                                int z = cfg.getInt("Commands." + commandName + ".z");
                                float yaw = Float.valueOf(cfg.getString("Commands." + commandName + ".yaw"));
                                float pitch = Float.valueOf(cfg.getString("Commands." + commandName + ".pitch"));
                                double delay = cfg.getDouble("Commands." + commandName + ".delay");
                                TPLOC = new LocationHelper(delay, x, y, z, yaw, pitch, world);
                        }
                        valid = true;
                } else {
                        valid = false;
                }
        }

        public CommandType getType() {
                if (!valid) return null;
                return type;
        }

        public String getCommand() {
                if (!valid) return null;
                return command;
        }

        public String getPermission() {
                if (!valid) return null;
                return permission;
        }

        public String getSendTO() {
                if (!valid) return null;
                return sendTo;
        }

        public List<String> getMessages() {
                if (!valid) return null;
                return messages;
        }

        public String getExecute() {
                if (!valid || type != CommandType.ALIAS) return null;
                return EXECUTE;
        }

        public LocationHelper getLocation() {
                if (!valid || type != CommandType.TELEPORT) return null;
                return TPLOC;
        }

        public void execute(Player player, String[] args) {
                if (!valid) return;
                String noperm = CommandManager.getNoPermissionMessage(player);
                if (!player.hasPermission(permission)) {
                        player.sendMessage(noperm);
                        return;
                }

                if (CommandManager.isBlocked(command, player.getWorld().getName())) {
                        String blocked = CommandManager.getCommandIsBlockedMessage(player, player.getWorld().getName(), command);
                        player.sendMessage(blocked);
                        return;
                }

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
                }

        }

        public void sendMessages(String[] args, Player player) {
                if (sendTo.equalsIgnoreCase("sender")) {
                        for (String s : messages) {
                                s = CommandManager.replaceValues(s, player, args);
                                player.sendMessage(s);
                        }
                } else if (sendTo.equalsIgnoreCase("online")) {
                        for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                for (String s : messages) {
                                        s = CommandManager.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else if (sendTo.equalsIgnoreCase("op")) {
                        for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                if (!p.isOp()) {
                                        continue;
                                }
                                for (String s : messages) {
                                        s = CommandManager.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else if (sendTo.equalsIgnoreCase("permission")) {
                        for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                if (!p.hasPermission(permission)) {
                                        continue;
                                }
                                for (String s : messages) {
                                        s = CommandManager.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else {
                        player.sendMessage("§c[ERROR] §7Wrong sendTo! You can use: online, sender, op, permission");
                }
        }

}
