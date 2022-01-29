package br.com.eskaryos.rankup.utils.api;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Papi extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "rankup";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ESKARYOS";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player p, String s){
        if(p==null)return "0";
        if(s.equals("rank")){
            return DataMain.getProfile(p.getUniqueId()).getRank().getDisplay();
        }
        if(s.equals("next")){
            Rank rank = DataMain.getProfile(p.getUniqueId()).getRank();
            if(RankMain.getFinalRank().getOrder()>=rank.getOrder()){
                return "§cLimit";
            }
            return Objects.requireNonNull(RankMain.getRankById(rank.getOrder() + 1)).getDisplay();
        }
        return "0";
    }
}
