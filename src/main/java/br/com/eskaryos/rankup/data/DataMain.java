package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

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

    public static MySQL mySQL;



    public static void LoadPlayerData(Player p){
        if(profileList.containsKey(p.getUniqueId()))return;
        if(mySQL!=null){
            mySQL.loadPlayer(p.getUniqueId());
        }else{
            SQLite.loadPlayer(p.getUniqueId());
        }
    }
    public static void UnloadPlayerData(Player p){
        if(!profileList.containsKey(p.getUniqueId()))return;
        if(profileList.get(p.getUniqueId()).getRank()==null)return;

        if(mySQL!=null){
            mySQL.setRank(p.getUniqueId(),profileList.get(p.getUniqueId()).getRank().getName());
            mySQL.setRequirement(p.getUniqueId(),profileList.get(p.getUniqueId()).getNext());
        }else{
            SQLite.setRank(p.getUniqueId(),profileList.get(p.getUniqueId()).getRank().getName());
            SQLite.setRequirement(p.getUniqueId(),profileList.get(p.getUniqueId()).getNext());
        }
        getProfileList().remove(p.getUniqueId());
    }

    public static String serializeRequirements(Rank rank){
        StringBuilder text = new StringBuilder();
        for (String r : rank.getRequirements().keySet()) {
            text.append(r+":"+rank.getRequirements().get(r).getValue()).append(",");
        }
        return text.toString();
    }
    public static void deserializeRequirement(String name,Rank rank){
        if(name.length()<=1)return;
        String[] key = name.split(",");
        for (String s : key) {
            String value = s.split(":")[1];
            String requirement = s.split(":")[0];
            if(rank.getRequirements().containsKey(requirement)){
                rank.getRequirements().get(requirement).setValue(Integer.parseInt(value));
            }
        }
    }

   /* public static void deserializeRequirements(Map<RequirementType,String> list,Rank rank){
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
    }*/

    public static void SetupData(){
        if(mySQL==null){
            SQLite.openSQL();
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            LoadPlayerData(p);
        }
    }
    public static void UnloadData(){
        for(Player p : Bukkit.getOnlinePlayers()){
            UnloadPlayerData(p);
        }
        if(mySQL==null){
            SQLite.close();
        }else{mySQL.close();}
    }
}
