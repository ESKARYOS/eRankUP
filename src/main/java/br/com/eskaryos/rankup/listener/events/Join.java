package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;


public class Join extends Listeners {


    @EventHandler
    public void event(PlayerJoinEvent e){
        Player p = e.getPlayer();
        DataMain.LoadPlayerData(p);
        JavaUtils.clearChat(p);
        if(!Lang.first_join){
            e.setJoinMessage(null);
            for(String s : Lang.joinMessage){
                p.sendMessage(RankHolder.hook(p, Utils.color(s)));
            }
        }
    }
}
