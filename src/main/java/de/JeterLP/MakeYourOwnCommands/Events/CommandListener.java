package de.JeterLP.MakeYourOwnCommands.Events;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.WrongTypeException;
import de.JeterLP.MakeYourOwnCommands.utils.CommandUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author JeterLP
 */
public final class CommandListener implements Listener {

        private final Main main;
        private CommandUtils utils;

        public CommandListener(Main main) {
                this.main = main;
        }

        /**
         * <p>
         * This method checks if a Player types a command that an administrator has
         * registered and sends the Player the messages
         * </p>
         *
         * @param event
         * @throws de.JeterLP.MakeYourOwnCommands.WrongTypeException
         */
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onCommand(final PlayerCommandPreprocessEvent event) throws WrongTypeException {
                if (event.isCancelled()) {
                        return;
                }
                utils = new CommandUtils(main);
                final Player player = event.getPlayer();
                final String[] args = event.getMessage().split(" ");
                final String command = args[0];

                if (!utils.isRegistered(command)) {
                        return;
                }

                String type = utils.getType(command);
                String permission = utils.getPermission(command);
                String noperm = utils.getNoPermissionMessage(player);
                String blocked = utils.getCommandIsBlockedMessage(player, player.getLocation().getWorld().getName(), command);

                if (!player.hasPermission(permission)) {
                        player.sendMessage(noperm);
                        event.setCancelled(true);
                        return;
                }
                
                if(utils.isBlocked(command, player.getWorld().getName())) {
                        player.sendMessage(blocked);
                        event.setCancelled(true);
                        return;
                }

                if (type.equalsIgnoreCase("alias")) {
                        String execute = "";
                        try {
                                execute = utils.getExecute(command, player, args);
                        } catch (WrongTypeException ex) {
                                Logger.getLogger(CommandListener.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        player.performCommand(execute);
                        sendMessages(command, args, player);
                        event.setCancelled(true);
                } else if (type.equalsIgnoreCase("message")) {
                        sendMessages(command, args, player);
                        event.setCancelled(true);
                } else if (type.equalsIgnoreCase("teleport")) {
                        final Location loc = utils.getTargetLocation(command);
                        double delay = utils.getDelay(command);
                        sendMessages(command, args, player);
                        main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                                @Override
                                public void run() {
                                        player.teleport(loc);
                                }
                        }, (long) (delay * 20.0));
                        event.setCancelled(true);
                }
        }

        private void sendMessages(String command, String[] args, Player player) {
                final String permission = utils.getPermission(command);
                final String sendto = utils.getSendTo(command);
                final List<String> messages = utils.getMessages(command, args, player);
                if (sendto.equalsIgnoreCase("sender")) {
                        for (String s : messages) {
                                s = utils.replaceValues(s, player, args);
                                player.sendMessage(s);
                        }
                } else if (sendto.equalsIgnoreCase("online")) {
                        for (Player p : main.getServer().getOnlinePlayers()) {
                                for (String s : messages) {
                                        s = utils.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else if (sendto.equalsIgnoreCase("op")) {
                        for (Player p : main.getServer().getOnlinePlayers()) {
                                if (!p.isOp()) {
                                        continue;
                                }
                                for (String s : messages) {
                                        s = utils.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else if (sendto.equalsIgnoreCase("permission")) {
                        for (Player p : main.getServer().getOnlinePlayers()) {
                                if (!p.hasPermission(permission)) {
                                        continue;
                                }
                                for (String s : messages) {
                                        s = utils.replaceValues(s, player, args);
                                        p.sendMessage(s);
                                }
                        }
                } else {
                        player.sendMessage("§c[ERROR] §7Wrong sendTo! You can use: online, sender, op, permission");
                }
        }
}
