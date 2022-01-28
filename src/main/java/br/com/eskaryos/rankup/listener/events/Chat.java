package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat extends Listeners {


    @EventHandler
    public void event(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String rank = "";

        if(DataMain.getProfile(p.getUniqueId())!=null){
            rank = DataMain.getProfile(p.getUniqueId()).getRank().getDisplay();
        }
        e.setFormat(rank+p.getDisplayName()+" §7§l>> §r§7" + e.getMessage());
    }
}
