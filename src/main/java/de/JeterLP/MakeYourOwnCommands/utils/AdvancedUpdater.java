package de.JeterLP.MakeYourOwnCommands.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class AdvancedUpdater {

        private String versionName;
        private final int id;
        private int task;
        private final URL url;
        private final JavaPlugin main;
        private int remoteVersion;
        private final int version;
        private String verwith, link, configValue;

        public AdvancedUpdater(JavaPlugin main, int id, String link, String configValue) throws MalformedURLException {
                this.main = main;
                this.id = id;
                this.version = Integer.valueOf(main.getDescription().getVersion().replaceAll("\\.", "").split("-")[0]);
                this.link = link;
                this.configValue = configValue;
                this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + this.id);
        }

        /**
         * Searches for an update and announces it, if an update was found.
         */
        public void search() {
                if (!isEnabled()) {
                        return;
                }
                task = main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                        @Override
                        public void run() {
                                if (!read()) {
                                        return;
                                }
                                if (versionCheck(versionName)) {
                                        main.getLogger().log(Level.INFO, "A new update is available! (" + verwith + ") current: " + main.getDescription().getVersion());
                                        main.getLogger().log(Level.INFO, "You can get it at: " + link);
                                }
                                main.getServer().getScheduler().cancelTask(task);
                        }
                }, 200L);
        }

        /**
         * Used to check if the last version fromk BukkitDev is higher than the current.
         *
         * @param title
         * @return true: If the version on BukkitDev is higher.
         */
        private boolean versionCheck(String title) {
                String[] titleParts = title.split(" v");
                this.remoteVersion = Integer.valueOf(titleParts[1].split(" ")[0].replaceAll("\\.", ""));
                this.verwith = titleParts[1].split(" ")[0];
                if (this.version > this.remoteVersion) {
                        return false;
                }
                return (this.version != this.remoteVersion) && (this.version <= this.remoteVersion);
        }

        /**
         * Reads all version out of Bukkits update-system.
         *
         * @return true: if the check was successfully.
         */
        private boolean read() {
                try {
                        URLConnection conn = this.url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setDoOutput(true);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String response = reader.readLine();
                        JSONArray array = (JSONArray) JSONValue.parse(response);
                        if (array.size() == 0) {
                                return false;
                        }
                        this.versionName = ((String) ((JSONObject) array.get(array.size() - 1)).get("name"));
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        /**
         * Checks if the UpdateChecker is enabled by the given config value in the constructor.
         *
         * @return true: if its enabled.
         */
        private boolean isEnabled() {
                return this.main.getConfig().getBoolean(configValue, true);
        }
}
