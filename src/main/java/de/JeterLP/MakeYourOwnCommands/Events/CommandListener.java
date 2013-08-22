package de.JeterLP.MakeYourOwnCommands.Events;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author JeterLP
 */
public class CommandListener implements Listener {

    Main plugin;
    Calendar calender = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    /**
     * <p>
     * This is the constructor needed to get the Main class
     * </p>
     *
     * @param p
     */
    public CommandListener(Main p) {
        plugin = p;
    }

    /**
     * <p>
     * This method checks if a Player types a command that an administrator has
     * registered and sends the Player the messages
     * </p>
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String[] args = event.getMessage().split(" ");
        for (String commandToCheck : plugin.getConfig().getConfigurationSection("commands").getKeys(false)) {
            if (commandToCheck.equalsIgnoreCase(args[0])) {
                String permission = plugin.getConfig().getString("commands." + args[0] + ".permission");
                String sendto = this.plugin.getConfig().getString("commands." + args[0] + ".sendTo");
                if (permission != null) {
                    if (player.hasPermission(permission) || player.hasPermission("myoc.*")) {
                        List<String> messages = plugin.getConfig().getStringList("commands." + args[0] + ".messages");
                        for (String com : messages) {
                            String command = com.replaceAll("%sender%", player.getName()).replaceAll("%realtime%", format.format(new Date())).replaceAll("%onlineplayers%", String.valueOf(Bukkit.getOnlinePlayers().length)).replaceAll("%world%", player.getWorld().getName());
                            command = command.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                            if ((sendto == null) || (sendto.equalsIgnoreCase("sender"))) {
                                player.sendMessage(command);
                            } else if (sendto.equalsIgnoreCase("all")) {
                                this.plugin.getServer().broadcastMessage(command);
                            } else if (sendto.equalsIgnoreCase("op")) {
                                for (Player online : Bukkit.getOnlinePlayers()) {
                                    if (online.isOp()) {
                                        online.sendMessage(command);
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage("§4You don't have permission.");
                    }
                    event.setCancelled(true);
                }
            }
        }
        for (String alias : this.plugin.getConfig().getConfigurationSection("aliases").getKeys(false)) {
            if (alias.equalsIgnoreCase(args[0])) {
                String command = this.plugin.getConfig().getString("aliases." + args[0]);
                event.setMessage(command);
            }
        }
        for (String tpcmds : this.plugin.getConfig().getConfigurationSection("Teleportations").getKeys(false)) {
            if (tpcmds.equalsIgnoreCase(args[0])) {
                String permission = plugin.getConfig().getString("Teleportations." + args[0] + ".permission");
                if (player.hasPermission(permission)) {
                    Long delay = plugin.getConfig().getLong("Teleportations." + args[0] + ".delay");
                    try {
                        event.setCancelled(true);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                String world = plugin.getConfig().getString("Teleportations." + args[0] + ".world");
                                double x = plugin.getConfig().getDouble("Teleportations." + args[0] + ".x");
                                double z = plugin.getConfig().getDouble("Teleportations." + args[0] + ".z");
                                double y = plugin.getConfig().getDouble("Teleportations." + args[0] + ".y");
                                player.teleport(new Location(Bukkit.getWorld(world), x, y, z));
                                player.sendMessage(plugin.getConfig().getString("Teleportations." + args[0] + ".message").replaceAll("&((?i)[0-9a-fk-or])", "§$1"));
                            }
                        },delay * 20);
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else {
                    player.sendMessage("§4You don't have permission.");
                }
            }
        }
    }
}
