package br.com.eskaryos.rankup.utils.api.placeholder;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiRequirementsPercent extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "percent";
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
        if(s.contains("all")){
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if(profile.getNext()==null) return "&cERROR";
            int value = profile.getNext().getTotalValue();
            int max = profile.getNext().getTotalMax();
            return JavaUtils.percent(value,max);
        }
        try{
            int value = Integer.parseInt(RequirementMain.getRequirementValue(p,s));
            int max = Integer.parseInt(RequirementMain.getRequirementMaxValue(p,s));
            return JavaUtils.percent(value,max);
        }catch (NumberFormatException e){
            return "&cERROR";
        }
    }
}
