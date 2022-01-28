package br.com.eskaryos.rankup.ranks;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.utils.JavaUtils;
import br.com.eskaryos.rankup.utils.Logger;
import br.com.eskaryos.rankup.utils.StringUtils;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.*;

public class RankMain {

    private static Map<String,Rank> rankMap = new HashMap<>();

    /**
     * Return Map<String,Rank>
     * */
    public static Map<String,Rank> getRankMap(){
        return rankMap;
    }

    /**
     * Return all Ranks in List
     * */
    public static List<Rank> getAllRanks(){
        return new ArrayList<>(rankMap.values());
    }


    /**
     * Get rank by String
     * @param name Rank name
     * @return return rank
     */
    public static Rank getRankByName(String name){
        return getRankMap().get(name);
    }

    /**
     * Return default rank
     * @return
     */
    public static Rank getDefaultRank(){
        return getRankById(0);
    }

    /***
     * Return final rank
     * @return
     */
    public static Rank getFinalRank(){
        return getRankById(getAllRanks().size()-1);
    }

    /***
     * Return rank by position
     * @param id Rank position
     * @return
     */
    public static Rank getRankById(int id){
        for(Rank r : getAllRanks()){
            if(r.getOrder() ==id){
                return r;
            }
        }
        return null;
    }

    public static void evolve(UUID uuid){
        Rank rank = DataMain.getProfile(uuid).getRank();
        Player p = Bukkit.getPlayer(uuid);
        if(rank.getOrder()>= getFinalRank().getOrder()){
            p.sendMessage(Lang.last_rank);
            JavaUtils.playSound(p,SoundsAPI.ENDERMAN_TELEPORT,1.5F,1.0F);
            return;
        }
        Rank next = getRankById(rank.getOrder()+1);
        if(next!=null){
            DataMain.getProfile(uuid).setRank(clone(next));
            p.sendMessage(Lang.evolvedMsg.replace("<rank>",next.getDisplay()));
            JavaUtils.sendAllMessage(Lang.evolvedGlobal.replace("<rank>",next.getDisplay()).replace("<player>",p.getDisplayName()));
            JavaUtils.sendAllSound(SoundsAPI.AMBIENCE_THUNDER,1F,1F);
        }
    }

    public static Rank clone(Rank rank){
        return new Rank(rank.getName(),rank.getDisplay(),rank.getOrder());
    }

    public static void loadRank(){
        File file = new File(Main.plugin.getDataFolder(),"ranks.yml");
        if(!file.exists()){Main.plugin.saveResource("ranks.yml",true);}
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Logger.log(Logger.LogLevel.INFO,"§aLoading all ranks");

        for(String key : Objects.requireNonNull(config.getConfigurationSection("Ranks")).getKeys(false)){

            String display = config.getString("Ranks."+key+".display").replace("&","§");
            int order = config.getInt("Ranks."+key+".order");

            Rank rank = new Rank(key,display,order);
            Logger.log(Logger.LogLevel.INFO,"§eRank §f" + key + " §ehas been uploaded successfully");
            getRankMap().put(key,rank);
        }
    }

}
