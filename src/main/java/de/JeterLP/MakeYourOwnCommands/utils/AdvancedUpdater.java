package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class AdvancedUpdater
{
  private String versionName;
  private final int id;
  private URL url;
  private final Main main;
  private int remoteVersion;
  private final int version;
  
  public AdvancedUpdater(Main main, int id)
  {
    this.main = main;
    this.id = id;
    this.version = Integer.valueOf(main.getDescription().getVersion().replaceAll("\\.", "")).intValue();
  }
  
  public void search()
  {
    if (!isEnabled())
    {
      MYOClogger.log(MYOClogger.Type.UPDATE, "Updater is disabled!");
      return;
    }
    try
    {
      this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + this.id);
    }
    catch (MalformedURLException ex)
    {
      MYOClogger.log(MYOClogger.Type.UPDATE, this.id + " is invalid.");
      return;
    }
    if (!read()) {
      return;
    }
    if (versionCheck(this.versionName))
    {
      MYOClogger.log(MYOClogger.Type.UPDATE, "A new update is available! (v" + this.remoteVersion + ") current: " + this.version);
      MYOClogger.log(MYOClogger.Type.UPDATE, "You can get it at: http://dev.bukkit.org/bukkit-plugin/simple-info2/");
    }
  }
  
  public void search(Player player)
  {
    try
    {
      this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + this.id);
    }
    catch (MalformedURLException ex)
    {
      player.sendMessage("§c[UPDATER]§7 " + this.id + " is invalid.");
      return;
    }
    if (!read()) {
      return;
    }
    if (versionCheck(this.versionName))
    {
      MYOClogger.log(MYOClogger.Type.UPDATE, "A new update is available! (v" + this.remoteVersion + ") current: " + this.version);
      MYOClogger.log(MYOClogger.Type.UPDATE, "You can get it at: http://dev.bukkit.org/bukkit-plugin/simple-info2/");
      player.sendMessage("§a[UPDATER]§7 A new update is available! (v" + this.remoteVersion + ") current: " + this.version);
      player.sendMessage("§a[UPDATER]§7 You can get it at: http://dev.bukkit.org/bukkit-plugin/simple-info2/");
    }
  }
  
  private boolean versionCheck(String title)
  {
    String[] titleParts = title.split(" v");
    this.remoteVersion = Integer.valueOf(titleParts[1].split(" ")[0].replaceAll("\\.", "")).intValue();
    if (this.version > this.remoteVersion) {
      return false;
    }
    return (this.version != this.remoteVersion) && (this.version <= this.remoteVersion);
  }
  
  private boolean read()
  {
    try
    {
      URLConnection conn = this.url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setDoOutput(true);
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = reader.readLine();
      
      JSONArray array = (JSONArray)JSONValue.parse(response);
      if (array.size() == 0) {
        return false;
      }
      this.versionName = ((String)((JSONObject)array.get(array.size() - 1)).get("name"));
      return true;
    }
    catch (Exception e) {}
    return false;
  }
  
  public boolean isEnabled()
  {
    return this.main.getConfig().getBoolean("SearchForUpdates", true);
  }
}
