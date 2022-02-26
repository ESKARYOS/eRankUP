package br.com.eskaryos.rankup.utils.api.placeholder;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RequirementsMax extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "max";
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
            int value = profile.getNext().getTotalMax();
            return value+"";
        }
        return RequirementMain.getRequirementMaxValue(p,s);
    }
}
