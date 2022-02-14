package br.com.eskaryos.rankup.utils.api.placeholder;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import br.com.eskaryos.rankup.requirements.RequirementType;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiRequirementsBar extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "bar";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ESKARYOS";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player p, @NotNull String s){
        if(p==null)return "0";
        if(s.contains("craft_")){
            String key = s.replace("craft_","");
            RequirementType type = RequirementType.CRAFT;
            try{
                int value = Integer.parseInt(RequirementMain.getRequirementValue(p,type,Integer.parseInt(key)));
                int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,type,Integer.parseInt(key)));
                return JavaUtils.makeProgressBar(value,max, Lang.color1,Lang.color2,Lang.barsize);
            }catch (NumberFormatException e){
                return "§cERROR";
            }
        }
        if(s.contains("mine_")){
            String key = s.replace("mine_","");
            RequirementType type = RequirementType.MINE;
            try{
                int value = Integer.parseInt(RequirementMain.getRequirementValue(p,type,Integer.parseInt(key)));
                int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,type,Integer.parseInt(key)));
                return JavaUtils.makeProgressBar(value,max, Lang.color1,Lang.color2,Lang.barsize);
            }catch (NumberFormatException e){
                return "§cERROR";
            }
        }
        if(s.contains("fish_")){
            String key = s.replace("fish_","");
            RequirementType type = RequirementType.FISH;
            try{
                int value = Integer.parseInt(RequirementMain.getRequirementValue(p,type,Integer.parseInt(key)));
                int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,type,Integer.parseInt(key)));
                return JavaUtils.makeProgressBar(value,max, Lang.color1,Lang.color2,Lang.barsize);
            }catch (NumberFormatException e){
                return "§cERROR";
            }
        }
        if(s.contains("place_")){
            String key = s.replace("place_","");
            RequirementType type = RequirementType.PLACE;
            try{
                int value = Integer.parseInt(RequirementMain.getRequirementValue(p,type,Integer.parseInt(key)));
                int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,type,Integer.parseInt(key)));
                return JavaUtils.makeProgressBar(value,max, Lang.color1,Lang.color2,Lang.barsize);
            }catch (NumberFormatException e){
                return "§cERROR";
            }
        }
        if(s.contains("kill_")){
            String key = s.replace("kill_","");
            RequirementType type = RequirementType.KILL;
            try{
                int value = Integer.parseInt(RequirementMain.getRequirementValue(p,type,Integer.parseInt(key)));
                int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,type,Integer.parseInt(key)));
                return JavaUtils.makeProgressBar(value,max, Lang.color1,Lang.color2,Lang.barsize);
            }catch (NumberFormatException e){
                return "§cERROR";
            }
        }
        if(s.contains("all")){
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if(profile.getNext()==null)return "§cERROR";
            Rank next = profile.getNext();
            return JavaUtils.makeProgressBar(next.getTotalValue(),next.getTotalMax(),Lang.color1,Lang.color2,Lang.barsize);
        }
        return "0";
    }
}
