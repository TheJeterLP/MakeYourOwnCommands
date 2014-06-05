package de.JeterLP.MakeYourOwnCommands.Listener;

import de.JeterLP.MakeYourOwnCommands.Command.CommandManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author JeterLP
 */
public final class CommandListener implements Listener {

    /**
     * Method to execute the Commands.
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage().split(" ")[0];

        if (!CommandManager.isRegistered(command)) {
            return;
        }

        String arg = "";

        for (int i = 1; i < event.getMessage().split(" ").length; i++) {
            arg += event.getMessage().split(" ")[i] + ";";
        }

        final String[] args = arg.split(";");

        CommandManager.getCommand(command).execute(player, args, true, true);
        event.setCancelled(true);
    }
}
