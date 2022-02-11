package br.com.eskaryos.rankup.listener;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.events.Chat;
import br.com.eskaryos.rankup.listener.events.Join;
import br.com.eskaryos.rankup.listener.events.Quit;
import br.com.eskaryos.rankup.menu.MenuListener;
import br.com.eskaryos.rankup.requirements.RequirementsEvents;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class Listeners implements Listener {

    public static void setupListener() {
        try {
            Main plugin = Main.plugin;
            PluginManager pm = Bukkit.getPluginManager();

            pm.registerEvents(new Join(), plugin);
            pm.registerEvents(new Quit(), plugin);
            pm.registerEvents(new Chat(), plugin);
            pm.registerEvents(new MenuListener(),plugin);
            pm.registerEvents(new RequirementsEvents(),plugin);
            Logger.log(Logger.LogLevel.SUCCESS, Lang.events_sucess);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR, Lang.events_error);
        }
    }


}

