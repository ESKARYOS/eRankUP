package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.utils.placeholder.RankHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join extends Listeners {


    @EventHandler
    public void event(PlayerJoinEvent e){
        Player p = e.getPlayer();
        DataMain.LoadPlayerData(p);

        for(int i = 0;i<=100;i++){
            p.sendMessage("");


            if(i==100){
                if(!Lang.first_join){
                    e.setJoinMessage(null);
                    for(String s : Lang.joinMessage){
                        p.sendMessage(RankHolder.hook(p,s));
                    }
                }
            }
        }


    }
}
