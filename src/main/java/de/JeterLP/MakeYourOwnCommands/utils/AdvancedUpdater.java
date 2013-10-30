package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import de.JeterLP.MakeYourOwnCommands.utils.MYOClogger.Type;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author TheJeterLP
 */
public class AdvancedUpdater {

    private String versionName;
    private final int id;
    private URL url;
    private final Main main;
    private int remoteVersion;
    private final int version;
    private String remoteVersionWithDot;

    public AdvancedUpdater(Main main, int id) {
        this.main = main;
        this.id = id;
        this.version = Integer.valueOf(main.getDescription().getVersion().replaceAll("\\.", ""));
    }

    /**
     * If an update is available, broadcast it.
     */
    public void search() {

        if (!isEnabled()) {
            MYOClogger.log(Type.UPDATE, "Updater is disabled!");
            return;
        }

        try {
            this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + id);
        } catch (MalformedURLException ex) {
            MYOClogger.log(Type.UPDATE, id + " is invalid.");
            return;
        }

        if (!read()) {
            return;
        }

        if (versionCheck(versionName)) {
            MYOClogger.log(Type.UPDATE, "A new update is available! (" + remoteVersionWithDot + ") current: " + main.getDescription().getVersion());
            MYOClogger.log(Type.UPDATE, "You can get it at: http://dev.bukkit.org/bukkit-plugin/simple-info2/");
        }
    }

    public void search(Player player) {

        try {
            this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + id);
        } catch (MalformedURLException ex) {
            player.sendMessage("§c[UPDATER]§7 " + id + " is invalid.");
            return;
        }

        if (!read()) {
            return;
        }

        if (versionCheck(versionName)) {
            player.sendMessage("§a[UPDATER]§7 " + "A new update is available! (" + remoteVersionWithDot + ") current: " + main.getDescription().getVersion());
            player.sendMessage("§a[UPDATER]§7 " + "You can get it at: http://dev.bukkit.org/bukkit-plugin/simple-info2/");
        }
    }

    /**
     * Checks if the remoteVersion is higher than the current version
     *
     * @param title
     * @return
     */
    private boolean versionCheck(String title) {
        String[] titleParts = title.split(" v");
        this.remoteVersion = Integer.valueOf(titleParts[1].split(" ")[0].replaceAll("\\.", ""));
        this.remoteVersionWithDot = titleParts[1].split(" ")[0];

        if (version > remoteVersion) {
            return false;
        }

        return version != remoteVersion && version <= remoteVersion;
    }

    /**
     * Sets the values
     *
     * @return boolean
     */
    private boolean read() {
        try {
            final URLConnection conn = this.url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();

            final JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() == 0) {
                return false;
            }

            this.versionName = (String) ((JSONObject) array.get(array.size() - 1)).get("name");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the updater was disabled in the configuration
     *
     * @return
     */
    public boolean isEnabled() {
        return main.getConfig().getBoolean("SearchForUpdates", true);
    }

}
