package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.Metrics.Metrics;
import java.io.IOException;

/**
 *
 * @author JeterLP
 */
public class MetricsChecker {

    private final Main main;

    /**
     * <p>This is the constructor needed to get the Main class</p>
     *
     * @param main
     */
    public MetricsChecker(Main main) {
        this.main = main;
    }

    /**
     * <p>This method sends the stats of this plugin to mcstats.org</p>
     */
    public void checkMetrics() {
        try {
            Metrics metrics = new Metrics(main);
            metrics.start();
        } catch (IOException ex) {
        }

    }
}
