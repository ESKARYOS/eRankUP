package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit extends Listeners {

    @EventHandler
    public void event(PlayerQuitEvent e){
        Player p = e.getPlayer();
        DataMain.UnloadPlayerData(p);
    }
}
