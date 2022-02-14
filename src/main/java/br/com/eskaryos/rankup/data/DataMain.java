package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.requirements.Requirement;
import br.com.eskaryos.rankup.requirements.RequirementType;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataMain {

    private static final Map<UUID,Profile> profileList = new HashMap<>();

    public static Profile getProfile(UUID uuid){
        return getProfileList().get(uuid);
    }

    public static Map<UUID,Profile> getProfileList(){
        return profileList;
    }

    public static List<Profile> getAllProfiles(){
        return new ArrayList<>(profileList.values());
    }

    public static void LoadPlayerData(Player p){
        if(profileList.containsKey(p.getUniqueId()))return;
        Database.loadPlayer(p.getUniqueId());
    }
    public static void UnloadPlayerData(Player p){
        if(!profileList.containsKey(p.getUniqueId()))return;
        if(profileList.get(p.getUniqueId()).getRank()==null)return;
        Database.setRank(p.getUniqueId(),profileList.get(p.getUniqueId()).getRank().getName());
        getProfileList().remove(p.getUniqueId());
    }


    public static void deserializeRequirements(Map<RequirementType,String> list,Rank rank){
        for(RequirementType type : list.keySet()){
            String line = list.get(type);
            Pattern pattern = Pattern.compile("(\\w+?),");
            Matcher matcher = pattern.matcher(line);
            int i = 0;
            while (matcher.find()) {
                String key = matcher.group(1);
                rank.getRequirements().get(type).get(i).setValue(Integer.parseInt(key));
                i++;
            }
        }
    }

    public static Map<RequirementType,String> serializeRequirements(Rank next){
        Map<RequirementType,String> listt = new HashMap<>();
        if(next!=null){
            Map<RequirementType, List<Requirement>> list = next.getRequirements().entrySet().stream()
                    .filter(a -> !a.getValue().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for(RequirementType type : list.keySet()){
                StringBuilder text = new StringBuilder();
                int i = 0;
                for(Requirement requirement : list.get(type)){
                    text.append(requirement.getValue()).append(",");
                }
                listt.put(type, text.toString());
            }
        }
        return listt;
    }

    public static void SetupData(){
        Database.openSQL();
        for(Player p : Bukkit.getOnlinePlayers()){
            LoadPlayerData(p);
        }
    }
    public static void UnloadData(){
        for(Player p : Bukkit.getOnlinePlayers()){
            UnloadPlayerData(p);
        }
        Database.close();
    }
}
