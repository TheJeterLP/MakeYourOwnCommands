package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.Metrics.Metrics;

/**
 *
 * @author JeterLP
 */
public class MetricsChecker {

    private final Main main = Main.instance;

    /**
     * <p>This method sends the stats of this plugin to mcstats.org</p>
     */
    public void checkMetrics() {
        try {
            Metrics metrics = new Metrics(main);
            metrics.start();
        } catch (Exception ex) {
        }

    }
}
