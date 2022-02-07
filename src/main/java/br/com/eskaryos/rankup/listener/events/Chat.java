package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.utils.bukkit.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat extends Listeners {


    @EventHandler
    public void event(AsyncPlayerChatEvent e){
        if(Lang.plugin_chat){
            Player p = e.getPlayer();
            String rank = "";
            String name = p.getDisplayName();
            String message = ColorUtils.translateColorCodes(e.getMessage());
            if(DataMain.getProfile(p.getUniqueId())!=null){
                rank = DataMain.getProfile(p.getUniqueId()).getRank().getDisplay();
            }
            e.setFormat(Lang.chatFormat.replace("<player>",name).replace("<rank>",rank).replace("<message>",message));
        }
    }
}
