package br.com.eskaryos.rankup;

import br.com.eskaryos.rankup.cmds.Commands;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import br.com.eskaryos.rankup.utils.placeholder.Papi;
import br.com.eskaryos.rankup.utils.placeholder.PapiRequirements;
import br.com.eskaryos.rankup.utils.placeholder.PapiRequirementsBar;
import br.com.eskaryos.rankup.utils.api.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    public static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        if(setupDependence()){
            try{
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================================");
                RankMain.loadRank();
                Listeners.setupListener();
                Commands.setupCommands();
                DataMain.SetupData();
                Lang.LoadFolders();
                RankMenu.LoadMenus();
                new Papi().register();
                new PapiRequirements().register();
                new PapiRequirementsBar().register();
                Logger.log(Logger.LogLevel.OUTLINE, "§aPlaceholdersAPI activated");
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================================");
                checkUpdate();
                System.gc();
            }catch (Exception e){}
        }
    }

    @Override
    public void onDisable() {
        DataMain.UnloadData();
    }


    private boolean setupDependence(){
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI")==null){
            try {
                Logger.log(Logger.LogLevel.OUTLINE, "§c============================================");
                Logger.log(Logger.LogLevel.OUTLINE, "§cPlugin disabled due to lack of PlaceholderAPI");
                Logger.log(Logger.LogLevel.OUTLINE, "§c============================================");
                getServer().getPluginManager().disablePlugin(this);
            } catch (Exception e) {
                getServer().getPluginManager().disablePlugin(this);
                Logger.log(Logger.LogLevel.OUTLINE, "§c============================================");
                Logger.log(Logger.LogLevel.OUTLINE, "§cPlugin disabled due to lack of PlaceholderAPI");
                Logger.log(Logger.LogLevel.OUTLINE, "§c============================================");
            }
            return false;
        }
        return true;
    }

    private void checkUpdate(){
        new UpdateChecker(this,94423).getLatestVersion(version ->{
            if(getDescription().getVersion().equalsIgnoreCase(version)){
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================");
                Logger.log(Logger.LogLevel.OUTLINE, "§ePlugin has been activated");
                Logger.log(Logger.LogLevel.OUTLINE, "§e  Plugin is up to date. ");
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================");
            } else {
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================================");
                Logger.log(Logger.LogLevel.OUTLINE, "§c     plugin has a new version available. ");
                Logger.log(Logger.LogLevel.OUTLINE, "§a============================================");

            }
        });
    }

}
