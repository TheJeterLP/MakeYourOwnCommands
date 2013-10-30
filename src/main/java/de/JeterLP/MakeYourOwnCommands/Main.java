package de.JeterLP.MakeYourOwnCommands;

import de.JeterLP.MakeYourOwnCommands.Events.CommandListener;
import de.JeterLP.MakeYourOwnCommands.Events.PlayerJoinListener;
import de.JeterLP.MakeYourOwnCommands.commands.myoc;
import de.JeterLP.MakeYourOwnCommands.utils.AdvancedUpdater;
import de.JeterLP.MakeYourOwnCommands.utils.ConfigFile;
import de.JeterLP.MakeYourOwnCommands.utils.MYOClogger;
import de.JeterLP.MakeYourOwnCommands.utils.MetricsChecker;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
        extends JavaPlugin {

    private ConfigFile loader;
    private MetricsChecker mchecker = null;
    public static Permission perms = null;
    public boolean useVault = false;
    private AdvancedUpdater updater;

    /**
     *
     */
    public void onEnable() {
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP Version: " + getDescription().getVersion() + ") loading...");
        this.loader = new ConfigFile(this);
        this.loader.loadConfig();
        this.updater = new AdvancedUpdater(this, 54353);
        search();
        if (checkVault()) {
            setupPermissions();
            this.useVault = true;
            MYOClogger.log(MYOClogger.Type.INFO, "Vault was found! Successfully hooked into: " + perms.getName());
        } else {
            MYOClogger.log(MYOClogger.Type.INFO, "Vault was not found! Vault-support is disabled...");
        }
        this.mchecker = new MetricsChecker(this);
        this.mchecker.checkMetrics();
        getCommand("myoc").setExecutor(new myoc(this));
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP Version: " + getDescription().getVersion() + ") is now enabled.");
    }

    @Override
    public void onDisable() {
        MYOClogger.log(MYOClogger.Type.INFO, "(by JeterLP Version: " + getDescription().getVersion() + ") is now disabled.");
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission) rsp.getProvider();
        return perms != null;
    }

    private boolean checkVault() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        return plugin != null;
    }

    public AdvancedUpdater getUpdater() {
        return this.updater;
    }

    private void search() {
        if (!this.updater.isEnabled()) {
            return;
        }
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                Main.this.updater.search();
            }
        }, 200L);
    }
}
