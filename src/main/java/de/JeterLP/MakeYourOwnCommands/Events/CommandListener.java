package de.JeterLP.MakeYourOwnCommands.Events;

import de.JeterLP.MakeYourOwnCommands.utils.CommandManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author JeterLP
 */
public final class CommandListener implements Listener {
        
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onCommand(final PlayerCommandPreprocessEvent event) {               
                final Player player = event.getPlayer();
                final String[] args = event.getMessage().split(" ");
                final String command = args[0];

                if(!CommandManager.isRegistered(command)) {
                        return;
                }
                
                CommandManager.getCommand(command).execute(player, args);
                event.setCancelled(true);
        }
}
