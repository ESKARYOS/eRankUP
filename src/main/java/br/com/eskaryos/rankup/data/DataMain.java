package br.com.eskaryos.rankup.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DataMain {

    private static Map<UUID,Profile> profileList = new HashMap<>();

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
        Database.setRank(p.getUniqueId(),profileList.get(p.getUniqueId()).getRank().getName());
        getProfileList().remove(p.getUniqueId());
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
