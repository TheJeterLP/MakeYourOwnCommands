package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class LocationHelper {

        private final int delay;
        private final Location loc;

        public LocationHelper(int delay, Location loc) {
                this.delay = delay;
                this.loc = loc;
        }

        public LocationHelper(int delay, int x, int y, int z, float yaw, float pitch, String World) {
                loc = new Location(Bukkit.getWorld(World), x, y, z, yaw, pitch);
                this.delay = delay;
        }

        /**
         * Teleports target to the location using the delay.
         * @param target 
         */
        public void teleport(final Player target) {
                if (delay == 0 || delay == -1) {
                        target.teleport(loc);
                        return;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                                target.teleport(loc);
                        }
                }, delay * 20);
        }

}
