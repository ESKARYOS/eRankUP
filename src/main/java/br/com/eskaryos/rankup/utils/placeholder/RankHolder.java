package br.com.eskaryos.rankup.utils.placeholder;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.bukkit.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;


public class RankHolder {

    public static String hook(Player p, String m){
        if(p==null)return "";

        String message = ColorUtils.translateStringColor(m);
        if(m.contains("<player>")){
            message = message.replace("<player>",p.getDisplayName());
        }
        if(m.contains("<rank>")){
            message = message.replace("<rank>",DataMain.getProfile(p.getUniqueId()).getRank().getDisplay());
        }
        if(m.contains("<next>")){
            Rank rank = DataMain.getProfile(p.getUniqueId()).getRank();
            if(rank.getOrder() >= RankMain.getFinalRank().getOrder()){
                message=  message.replace("<next>", Lang.lastRankVariable);
            }else{
                if(DataMain.getProfile(p.getUniqueId()).getNext()!=null){
                    message = m.replace("<next>",DataMain.getProfile(p.getUniqueId()).getNext().getDisplay());
                }else{
                    message = message.replace("<next>","§cERROR");
                }
            }
        }
        return PlaceholderAPI.setPlaceholders(p,message);
    }


}
