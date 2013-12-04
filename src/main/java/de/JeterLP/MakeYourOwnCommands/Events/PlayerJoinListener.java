package de.JeterLP.MakeYourOwnCommands.Events;

import de.JeterLP.MakeYourOwnCommands.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener
        implements Listener {

    private final Main main;

    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOPJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (!this.main.getUpdater().isEnabled()) {
            return;
        }
        this.main.getUpdater().search(player);
    }
}
