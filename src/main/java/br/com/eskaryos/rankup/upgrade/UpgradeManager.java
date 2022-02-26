package br.com.eskaryos.rankup.upgrade;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.requirements.Requirement;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import br.com.eskaryos.rankup.requirements.RequirementType;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpgradeManager extends Utils {

    private static final Map<String,Upgrade> registredupgrades = new HashMap<>();


    public static void loadUpgrades(){
        ItemStack test = JavaUtils.add(Material.EXPERIENCE_BOTTLE,1,0,"§dRequirement impulse",
                Arrays.asList("§7This item will give you" ,"§7a 100 block boost in requirements.","",
                        "§7Compatible: §eMine Requirement","§7Value: 100 blocks"));
        Upgrade upgrade = new Upgrade(RequirementType.MINE,100,test);
        getRegistredupgrades().put("test",upgrade);
    }

    public static void useUpgrade(Player p, Upgrade upgrade, Requirement requirement){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;
        if(requirement.getValue()>=requirement.getMax()){
            profile.getNext().sendEvolveSoundError(p);
            return;
        }
        if(requirement.getValue()+upgrade.getValue()>requirement.getMax()){
            requirement.setValue(requirement.getMax());
        }else{requirement.setValue(requirement.getValue()+upgrade.getValue());}
        RequirementMain.sendCompletedMessage(p,requirement);

        removeItems(p,1,upgrade.getIcon().clone());
    }

    public List<Upgrade> getUpgradeByType(RequirementType type){
        return registredupgrades.values().stream().filter(upgrade -> upgrade.getType()==type).collect(Collectors.toList());
    }

    public static Upgrade getUpgrade(ItemStack item){
        List<Upgrade> list = registredupgrades.values().stream().filter(upgrade -> upgrade.getIcon().isSimilar(item)).collect(Collectors.toList());
        if(list.isEmpty())return null;
        return list.get(0);
    }
    public static boolean hasUpgrade(ItemStack item){
        List<Upgrade> list = registredupgrades.values().stream().filter(upgrade -> upgrade.getIcon().isSimilar(item)).collect(Collectors.toList());
        return !list.isEmpty();
    }

    public static Upgrade getUpgrade(String string){
        return registredupgrades.get(string);
    }

    public static Map<String,Upgrade> getRegistredupgrades(){
        return registredupgrades;
    }

}
