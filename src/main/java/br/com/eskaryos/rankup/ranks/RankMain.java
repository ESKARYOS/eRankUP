package br.com.eskaryos.rankup.ranks;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.utils.ItemUtils;
import br.com.eskaryos.rankup.utils.JavaUtils;
import br.com.eskaryos.rankup.utils.Logger;
import br.com.eskaryos.rankup.utils.StringUtils;
import br.com.eskaryos.rankup.utils.api.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void evolve(UUID uuid,Rank evolve){
        Rank rank = DataMain.getProfile(uuid).getRank();
        Player p = Bukkit.getPlayer(uuid);

        if(rank.getOrder()>= getFinalRank().getOrder() && evolve.getOrder()==rank.getOrder()){
            p.sendMessage(RankHolder.hook(p,Lang.last_rank));
            rank.sendEvolveSoundError(p);
            return;
        }

        if(rank.getOrder() == evolve.getOrder()){
            p.sendMessage(RankHolder.hook(p,Lang.evolveError));
            rank.sendEvolveSoundError(p);
            return;
        }

        if(rank.getOrder()>evolve.getOrder()){
            p.sendMessage(RankHolder.hook(p,Lang.downgrade));
            rank.sendEvolveSoundError(p);
            return;
        }

        if(rank.getOrder()-evolve.getOrder()>1){
            p.sendMessage(RankHolder.hook(p,Lang.cantJump));
            rank.sendEvolveSoundError(p);
            return;
        }


        Rank next = getRankById(rank.getOrder()+1);
        if(next!=null){
            DataMain.getProfile(uuid).setRank(clone(next));
            next.sendEvolveMessage(p);
            next.sendAllEvolveMessage(p);
            assert p != null;
            next.sendEvolveSound(p);
            next.sendAllEvolveSound();

            p.getInventory().addItem(next.getRankIcon());
            p.getInventory().addItem(next.getRankIconCompleted());

            next.executeCommand(p);
        }
    }

    public static Rank clone(Rank rank){
        return rank.clone();
    }

    public static void loadRank(){
        File path = new File(Main.plugin.getDataFolder() + "/ranks");
        File[] list = path.listFiles();
        assert list != null;
        for(File file : list){
            if(file.getName().endsWith(".yml")){
                try{
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String display = config.getString("display").replace("&","§");
                    String name = config.getString("name").replace("&","§");
                    int order = config.getInt("order");

                    Rank rank = new Rank(name,display,order);
                    ItemStack icon = ItemUtils.getItem(config,"icon");
                    ItemStack iconCompleted = ItemUtils.getItem(config,"icon-completed");

                    SoundsAPI evolve = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-sound")));
                    SoundsAPI evolveAll = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-sound-global")));
                    SoundsAPI evolveError = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-sound-error")));

                    List<String> evolveMessage = convert(config.getStringList("evolve-message"));
                    List<String> evolveMessageAll = convert(config.getStringList("evolve-message-global"));

                    rank.setRankIcon(icon);
                    rank.setRankIconCompleted(iconCompleted);

                    rank.setEvolveSound(evolve);
                    rank.setEvolveSoundError(evolveError);
                    rank.setEvolveSoundAll(evolveAll);

                    rank.setCommands(Objects.requireNonNull(config.getStringList("commands")));

                    rank.setEvolveMessageAll(evolveMessageAll);
                    rank.setEvolveMessage(evolveMessage);

                    if(getRankById(order)!=null){
                        Logger.log(Logger.LogLevel.ERROR,"§cCould not load §f"+ file.getName()+"§c rank because there is already a rank with order §f" + order);
                    }else if(getRankByName(name)!=null){
                        Logger.log(Logger.LogLevel.ERROR,"§cCould not load §f"+ file.getName()+"§c rank because there is already a rank with name §f" + name);
                    }else{
                        Logger.log(Logger.LogLevel.INFO,"§eRank §f" + name + " §ehas been uploaded successfully");
                        getRankMap().put(name,rank);
                    }
                }catch (Exception e){
                    Logger.log(Logger.LogLevel.ERROR,e.getMessage());
                }
            }
        }
    }
    public static List<String> convert(List<String> l1){
        List<String> list = new ArrayList<>();
        for(String s : l1){list.add(s.replace("&","§"));}
        return list;
    }
}
