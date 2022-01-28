package br.com.eskaryos.rankup.listener;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.listener.events.Chat;
import br.com.eskaryos.rankup.listener.events.Join;
import br.com.eskaryos.rankup.listener.events.Quit;
import br.com.eskaryos.rankup.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class Listeners implements Listener {

    static String log = "[" + Main.plugin.getName() + "]";

    public static void setupListener() {
        try {
            Main plugin = Main.plugin;
            PluginManager pm = Bukkit.getPluginManager();

            pm.registerEvents(new Join(), plugin);
            pm.registerEvents(new Quit(), plugin);
            pm.registerEvents(new Chat(), plugin);
            Logger.log(Logger.LogLevel.SUCCESS, ChatColor.GREEN + log + " Eventos registrados com sucesso.");
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR, ChatColor.RED + log + " Não conseguiu registrar os eventos.");
        }
    }

}
