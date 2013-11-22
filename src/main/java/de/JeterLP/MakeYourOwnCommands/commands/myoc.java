package de.JeterLP.MakeYourOwnCommands.commands;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.utils.CommandUtils;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author JeterLP
 */
public class myoc implements CommandExecutor {

    private final Main main;

    public myoc(Main main) {
        this.main = main;
    }

    /**
     * <p>This method reloads the config if /myoc reload was typed</p>
     *
     * @param sender
     * @param cmd
     * @param commandlabel
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("myoc")) {
            if (args.length == 0) {
                sender.sendMessage("§e[MakeYourOwnCommands] by §aJeterLP §eversion: §c" + main.getDescription().getVersion());
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("myoc.reload") || sender.hasPermission("myoc.*")) {
                    main.reloadConfig();
                    sender.sendMessage("§aSuccesfully reloaded!");
                    return true;
                } else {
                    sender.sendMessage("§4You dont have permission!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (sender.hasPermission("myoc.list") || sender.hasPermission("myoc.*")) {
                    sender.sendMessage("§eMakeYourOwnCommands: §cCommands:");
                    for (String commands : main.getConfig().getConfigurationSection("Commands").getKeys(false)) {
                        List<String> messages = main.getConfig().getStringList("Commands." + commands + ".messages");
                        sender.sendMessage("§a" + commands + ":");
                        for (int i = 0; i < messages.size(); i++) {
                            sender.sendMessage("  " + messages.get(i).replaceAll("&((?i)[0-9a-fk-or])", "§$1"));

                        }
                    }
                    return true;
                } else {
                    sender.sendMessage("§4You dont have permission!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("location")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Location loc = p.getLocation();
                    sender.sendMessage("§aX: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + " YAW: " + loc.getYaw() + " PITCH: " + loc.getPitch() + " World: " + loc.getWorld().getName());
                    return true;
                } else {
                    sender.sendMessage("§cYou have to be a player for that!");
                    return true;
                }
            } else {
                sender.sendMessage("§cUsage: §e/myoc <reload|list|location>");
                return true;
            }
        }
        return false;
    }
}
