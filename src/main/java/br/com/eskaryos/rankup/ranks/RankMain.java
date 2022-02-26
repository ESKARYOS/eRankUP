package br.com.eskaryos.rankup.ranks;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.menu.Menu;
import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.requirements.Requirement;
import br.com.eskaryos.rankup.requirements.RequirementType;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import br.com.eskaryos.rankup.utils.bukkit.ItemUtils;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class RankMain extends Utils {

    private static final Map<String,Rank> rankMap = new HashMap<>();

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

    public static boolean isLastRank(Player p){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        return profile.getRank().getOrder() >= RankMain.getFinalRank().getOrder();
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
     * @return default rank
     */
    public static Rank getDefaultRank(){
        return getRankById(0);
    }

    /***
     * Return final rank
     * @return final rank
     */
    public static Rank getFinalRank(){
        return getRankById(getAllRanks().size()-1);
    }

    /***
     * Return rank by position
     * @param id Rank position
     * @return Rank by id
     */
    public static Rank getRankById(int id){
        for(Rank r : getAllRanks()){
            if(r.getOrder() ==id){
                return r;
            }
        }
        return null;
    }

    public static boolean hascompleted(Player p){
        Rank rank = DataMain.getProfile(p.getUniqueId()).getNext();
        for(Requirement requirement : rank.getRequirements().values()){
            if(requirement.getValue()<requirement.getMax()){
                p.sendMessage(Lang.requirementError.replace("<requirement>",requirement.getName()));
                return false;
            }
        }
        return true;
    }

    /**
     * Execute evolve rank
     * @param uuid Player UUID
     * @param evolve Rank to evolve
     */
    public static void evolve(UUID uuid,Rank evolve){
        Rank rank = DataMain.getProfile(uuid).getRank();
        Player p = Bukkit.getPlayer(uuid);
        if(DataMain.getProfile(uuid).getNext()==null)return;
        assert p!=null;
        if(isLastRank(p)){p.sendMessage(RankHolder.hook(p,Lang.last_rank));rank.sendEvolveSoundError(p);return;}
        if(rank.getOrder()>evolve.getOrder()){p.sendMessage(RankHolder.hook(p,Lang.downgrade));rank.sendEvolveSoundError(p);return;}
        if(rank.getOrder()-evolve.getOrder()>1){p.sendMessage(RankHolder.hook(p,Lang.cantJump));rank.sendEvolveSoundError(p);return;}
        if(!hascompleted(p)){
            DataMain.getProfile(uuid).getNext().sendEvolveSoundError(p);return;}
        DataMain.getProfile(uuid).setRank(clone(evolve));
        if(evolve.getOrder()<RankMain.getFinalRank().getOrder()){
            DataMain.getProfile(p.getUniqueId()).setNext(RankMain.getRankById(evolve.getOrder()+1));
        }
        evolve.sendEvolveMessage(p);
        evolve.sendAllEvolveMessage(p);

        evolve.sendEvolveSound(p);
        evolve.sendAllEvolveSound();

        evolve.sendEvolveBar(p);
        evolve.sendEvolveBarAll(p);

        evolve.sendEvolveTitle(p);
        evolve.executeCommand(p);
    }

    /***
     * Clone  rank to avoid bugs
     * @param rank rank to clone
     * @return return rank cloned;
     */
    public static Rank clone(Rank rank){
        return rank.clone();
    }

    public static void loadRank(){
        File path = new File(Main.plugin.getDataFolder() + "/ranks");
        if(!path.exists()){
            Main.plugin.saveResource("ranks/default.yml",true);
            Main.plugin.saveResource("ranks/fish1.yml",true);
            Main.plugin.saveResource("ranks/fish2.yml",true);
            Main.plugin.saveResource("ranks/fish3.yml",true);
            Main.plugin.saveResource("ranks/cow1.yml",true);
            Main.plugin.saveResource("ranks/cow2.yml",true);
            Main.plugin.saveResource("ranks/cow3.yml",true);
        }
        File[] list = path.listFiles();
        assert list != null;
        if(list.length<=0){
            Main.plugin.saveResource("ranks/default.yml",true);
            Main.plugin.saveResource("ranks/fish1.yml",true);
            Main.plugin.saveResource("ranks/fish2.yml",true);
            Main.plugin.saveResource("ranks/fish3.yml",true);
            Main.plugin.saveResource("ranks/cow1.yml",true);
            Main.plugin.saveResource("ranks/cow2.yml",true);
            Main.plugin.saveResource("ranks/cow3.yml",true);
        }
        for(File file : list){
            if(file.getName().endsWith(".yml")){
                try{
                    YamlConfiguration config = JavaUtils.loadConfigUTF8(file);
                    String display = color(config.getString("display"));
                    String name = color(config.getString("name"));
                    int order = config.getInt("order");

                    Rank rank = new Rank(name,display,order);
                    ItemStack icon = ItemUtils.getItem(config,"icon");
                    ItemStack iconCompleted = ItemUtils.getItem(config,"icon-completed");

                    Sound evolve = getSound(config.getString("evolve-sound"));
                    Sound evolveAll =getSound(config.getString("evolve-sound-global"));
                    Sound evolveError = getSound(config.getString("evolve-sound-error"));

                    List<String> evolveMessage = color(config.getStringList("evolve-message"));
                    List<String> evolveMessageAll = color(config.getStringList("evolve-message-global"));
                    rank.setEvolveActionbar(color(config.getString("action-bar")));
                    rank.setEvolveActionbarAll(color(config.getString("action-bar-all")));
                    rank.setEvolveTitle(color(config.getString("title")));
                    rank.setEvolveSubTitle(color(config.getString("subtitle")));

                    rank.setRankIcon(icon);
                    rank.setRankIconCompleted(iconCompleted);

                    rank.setEvolveSound(evolve);
                    rank.setEvolveSoundError(evolveError);
                    rank.setEvolveSoundAll(evolveAll);

                    rank.setCommands(Objects.requireNonNull(config.getStringList("commands")));

                    rank.setEvolveMessageAll(evolveMessageAll);
                    rank.setEvolveMessage(evolveMessage);
                    Menu menu = new Menu(display,config.getInt("menu.size"),1);
                    for(String key : Objects.requireNonNull(config.getConfigurationSection("menu.items")).getKeys(false)){
                        menu.getItems().put(key, RankMenu.getItem(config,"menu.items."+key));
                        menu.getItemSlot().put(key,config.getInt("menu.items."+key+".slot"));
                    }
                    for(String key : Objects.requireNonNull(config.getConfigurationSection("requirements")).getKeys(false)){
                        RequirementType type = RequirementType.valueOf(config.getString("requirements."+key+".type").toUpperCase(Locale.ROOT));
                        List<String> completedMessage = color(config.getStringList("requirements."+key+".message"));
                        SoundsAPI sound = SoundsAPI.valueOf(config.getString("requirements."+key+".sound"));
                        String title = color(config.getString("requirements."+key+".title"));
                        String bar = color(config.getString("requirements."+key+".actionbar"));
                        String reqName = color(config.getString("requirements."+key+".name"));
                        if(type == RequirementType.KILL){
                            String entity = config.getString("requirements."+key+".entity");
                            int max = config.getInt("requirements."+key+".value");
                            Requirement req = new Requirement(reqName,type, max, completedMessage, title, bar, sound);
                            req.setEntity(entity);
                            rank.getRequirements().put(key,req);
                            rank.getRequirementsbytype().get(type).add(req);
                        }else if(type ==RequirementType.ENCHANT){
                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(Objects.requireNonNull(config.getString("requirements." + key + ".enchantment"))));
                            int max = config.getInt("requirements."+key+".value");
                            ItemStack item = RankMenu.getItem(config,"requirements."+key+".item");
                            Requirement req =new Requirement(reqName,type,max,completedMessage,title,bar,sound);
                            req.setEnchantment(enchantment);
                            req.setItem(item);
                            rank.getRequirements().put(key,req);
                            rank.getRequirementsbytype().get(type).add(req);
                        }else{
                            ItemStack item = RankMenu.getItem(config,"requirements."+key+".item");
                            int max = config.getInt("requirements."+key+".value");
                            Requirement req = new Requirement(reqName,type, max, completedMessage, title, bar, sound);
                            req.setItem(item);
                            rank.getRequirements().put(key,req);
                            rank.getRequirementsbytype().get(type).add(req);
                        }

                    }

                    rank.setMenu(menu);
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
}
