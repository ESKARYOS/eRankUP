package br.com.eskaryos.rankup;

import br.com.eskaryos.rankup.cmds.Commands;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.ranks.RankMain;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    public static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        RankMain.loadRank();
        Listeners.setupListener();
        Commands.setupCommands();
        DataMain.SetupData();
        Lang.LoadLang();
    }

    @Override
    public void onDisable() {
        DataMain.UnloadData();
    }

}
