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
                                double delay = cfg.getDouble("Commands." + commandName + ".delay");
                                TPLOC = new LocationHelper(delay, x, y, z, yaw, pitch, world);
                        }
                        valid = true;
                } else {
                        Main.getInstance().getLogger().severe(commandName + " does not have a valid CommandType.");
                        valid = false;
                }
        }

        /**
         * @return CommandType
         */
        public CommandType getType() {
                if (!valid) return null;
                return type;
        }

        /**
         * @return CommandType
         */
        public String getCommand() {
                if (!valid) return null;
                return command;
        }

        /**
         * @return CommandType
         */
        public String getPermission() {
                if (!valid) return null;
                return permission;
        }

        /**
         * @return CommandType
         */
        public String getSendTO() {
                if (!valid) return null;
                return sendTo;
        }

        /**
         * @return CommandType
         */
        public List<String> getMessages() {
                if (!valid) return null;
                return messages;
        }

        /**
         * @return CommandType
         */
        public String getExecute() {
                if (!valid || type != CommandType.ALIAS) return null;
                return EXECUTE;
        }

        /**
         * @return CommandType
         */
        public LocationHelper getLocation() {
                if (!valid || type != CommandType.TELEPORT) return null;
                return TPLOC;
        }

        /**
         * @param player
         * @param args
         */
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

        /**
         * @param args
         * @param player
         */
        public void sendMessages(String[] args, Player player) {
                switch (sendTo) {
                        case "sender":
                                for (String s : messages) {
                                        s = CommandManager.replaceValues(s, player, args);
                                        player.sendMessage(s);
                                }
                                break;
                        case "online":
                                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                        for (String s : messages) {
                                                s = CommandManager.replaceValues(s, player, args);
                                                p.sendMessage(s);
                                        }
                                }
                                break;
                        case "op":
                                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                        if (!p.isOp()) {
                                                continue;
                                        }
                                        for (String s : messages) {
                                                s = CommandManager.replaceValues(s, player, args);
                                                p.sendMessage(s);
                                        }
                                }
                                break;
                        case "permission":
                                for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
                                        if (!p.hasPermission(permission)) {
                                                continue;
                                        }
                                        for (String s : messages) {
                                                s = CommandManager.replaceValues(s, player, args);
                                                p.sendMessage(s);
                                        }
                                }
                                break;
                        default:
                                player.sendMessage("§c[ERROR] §7Wrong sendTo! You can use: online, sender, op, permission");
                                break;
                        
                }
        }
        
}
