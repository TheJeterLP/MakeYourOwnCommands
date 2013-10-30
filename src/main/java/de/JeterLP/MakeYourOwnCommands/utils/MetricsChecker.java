package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.Metrics.Metrics;
import de.JeterLP.MakeYourOwnCommands.Metrics.Metrics.Graph;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JeterLP
 */
public class MetricsChecker {

    private Main main;

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
