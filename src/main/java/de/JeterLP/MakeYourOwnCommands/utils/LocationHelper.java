package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class LocationHelper {

        private final double delay;
        private final Location loc;

        public LocationHelper(double delay, Location loc) {
                this.delay = delay;
                this.loc = loc;
        }

        public LocationHelper(double delay, int x, int y, int z, float yaw, float pitch, String World) {
                loc = new Location(Bukkit.getWorld(World), x, y, z, yaw, pitch);
                this.delay = delay;
        }

        public void teleport(final Player target) {
                if (delay == 0.0 || delay == -1.0) {
                        target.teleport(loc);
                        return;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                                target.teleport(loc);
                        }
                }, (long) delay);
        }

}
