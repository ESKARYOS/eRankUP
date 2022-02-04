package br.com.eskaryos.rankup.utils.placeholder;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import br.com.eskaryos.rankup.requirements.RequirementType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PapiRequirements extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "value";
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
        if(s.contains("craft_")){
            String key = s.replace("craft_","");
            RequirementType type = RequirementType.CRAFT;
            return RequirementMain.getRequirementValue(p,type,Integer.parseInt(key));
        }
        if(s.contains("mine_")){
            String key = s.replace("mine_","");
            RequirementType type = RequirementType.MINE;
            return RequirementMain.getRequirementValue(p,type,Integer.parseInt(key));
        }
        if(s.contains("place_")){
            String key = s.replace("place_","");
            RequirementType type = RequirementType.PLACE;
            return RequirementMain.getRequirementValue(p,type,Integer.parseInt(key));
        }
        if(s.contains("pickup_")){
            String key = s.replace("pickup_","");
            RequirementType type = RequirementType.PICKUP;
            return RequirementMain.getRequirementValue(p,type,Integer.parseInt(key));
        }
        if(s.contains("kill_")){
            String key = s.replace("kill_","");
            RequirementType type = RequirementType.KILL;
            return RequirementMain.getRequirementValue(p,type,Integer.parseInt(key));
        }
        return "0";
    }
}
