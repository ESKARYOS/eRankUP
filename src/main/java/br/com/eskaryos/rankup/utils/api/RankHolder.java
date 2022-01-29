package br.com.eskaryos.rankup.utils.api;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Objects;

public class RankHolder {

    public static String hook(Player p, String m){
        if(p==null)return "";

        String message = m;
        if(m.contains("<player>")){
            message = message.replace("<player>",p.getDisplayName());
        }
        if(m.contains("<rank>")){
            message = message.replace("<rank>",DataMain.getProfile(p.getUniqueId()).getRank().getDisplay());
        }
        if(m.contains("<next>")){
            Rank rank = DataMain.getProfile(p.getUniqueId()).getRank();
            if(RankMain.getFinalRank().getOrder()>=rank.getOrder()){
                message=  message.replace("<next>","§cLimit");
            }else{
                message = m.replace("<next>",Objects.requireNonNull(RankMain.getRankById(rank.getOrder() + 1)).getDisplay());
            }
        }
        return PlaceholderAPI.setPlaceholders(p,message);
    }


}
