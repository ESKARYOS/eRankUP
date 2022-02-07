package br.com.eskaryos.rankup.ranks;


import br.com.eskaryos.rankup.menu.Menu;
import br.com.eskaryos.rankup.requirements.Requirement;
import br.com.eskaryos.rankup.requirements.RequirementType;
import br.com.eskaryos.rankup.utils.api.ActionBar;
import br.com.eskaryos.rankup.utils.api.Title;
import br.com.eskaryos.rankup.utils.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter@Setter
public class Rank {

    private String name;
    private String display;
    private int order;

    private SoundsAPI evolveSound;
    private SoundsAPI evolveSoundAll;
    private SoundsAPI evolveSoundError;

    private List<String> evolveMessage = new ArrayList<>();;
    private List<String> evolveMessageAll = new ArrayList<>();

    private String evolveTitle;
    private String evolveSubTitle;
    private String evolveActionbar;
    private String evolveActionbarAll;

    private List<String> commands = new ArrayList<>();

    private Map<RequirementType,List<Requirement>> requirements;

    private ItemStack rankIcon;
    private ItemStack rankIconCompleted;

    private Menu menu;

    public Rank(String name,String display,int order){
        this.name = name;
        this.display = display;
        this.order = order;
        requirements = new HashMap<>();
        requirements.put(RequirementType.CRAFT,new ArrayList<>());
        requirements.put(RequirementType.MINE,new ArrayList<>());
        requirements.put(RequirementType.PICKUP,new ArrayList<>());
        requirements.put(RequirementType.PLACE,new ArrayList<>());
        requirements.put(RequirementType.KILL,new ArrayList<>());
    }

    public void sendEvolveBar(Player p){
        ActionBar.sendActionBar(p,RankHolder.hook(p,evolveActionbar));
    }
    public void sendEvolveBarAll(Player p){
        for(Player g : Bukkit.getOnlinePlayers()){
            if(p!=g){
                ActionBar.sendActionBar(g,RankHolder.hook(p,evolveActionbarAll));
            }
        }
    }
    public void sendEvolveTitle(Player p){
        Title.sendTitle(p,evolveTitle,evolveSubTitle,0,20*5,0);
    }

    public int getTotalMax(){
        int value = 0;
        for(RequirementType type : getRequirements().keySet()){
            for(Requirement requirement : getRequirements().get(type)){
                value = value+requirement.getMax();
            }
        }
        return value;
    }
    public int getTotalValue(){
        int value = 0;
        for(RequirementType type : getRequirements().keySet()){
            for(Requirement requirement : getRequirements().get(type)){
                value = value+requirement.getValue();
            }
        }
        return value;
    }

    /**
     * Method to clone rank
     */
    public Rank clone(){
        Rank r = new Rank(getName(),getDisplay(),getOrder());
        r.setEvolveSound(getEvolveSound());
        r.setEvolveSoundAll(getEvolveSoundAll());
        r.setEvolveSoundError(getEvolveSoundError());
        r.setEvolveMessage(getEvolveMessage());
        r.setEvolveMessageAll(getEvolveMessageAll());
        r.setRankIcon(getRankIcon().clone());
        r.setRankIconCompleted(getRankIconCompleted().clone());
        r.setCommands(getCommands());

        for(RequirementType type : getRequirements().keySet()){
            for(Requirement requirement : getRequirements().get(type)){
                r.getRequirements().get(type).add(requirement);
            }
        }

        return r;
    }
    /**
     * Method to send player evolve message
     * @param p player who evolve
     */
    public void sendAllEvolveMessage(Player p){
        for(Player g : Bukkit.getOnlinePlayers()){
            for(String message : evolveMessageAll){
                if(g!=p){
                    g.sendMessage(RankHolder.hook(p,message));
                }
            }
        }
    }
    /**
     * Method to send player evolve message
     * @param p
     */
    public void sendEvolveMessage(Player p){
        for(String message : evolveMessage){
            p.sendMessage(RankHolder.hook(p,message));
        }
    }

    /**
     * Method to sendAll evolve sound
     */
    public void sendAllEvolveSound(){
        if(getEvolveSoundAll()==SoundsAPI.DISABLED)return;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSoundAll().bukkitSound()),1F,1F);
        }
    }
    /**
     * Method to send sound evolve
     * @param p
     */
    public void sendEvolveSound(Player p){
        if(getEvolveSound()==SoundsAPI.DISABLED)return;
        p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSound().bukkitSound()),1F,1F);
    }

    /**
     * Method to send sound error
     * @param p
     */
    public void sendEvolveSoundError(Player p){
        if(getEvolveSoundError()==SoundsAPI.DISABLED)return;
        p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSoundError().bukkitSound()),1F,1F);
    }

    /**
     * Method to execute evolution commands
     * @param p Player who execute
     */
    public void executeCommand(Player p){
        for(String r : getCommands()){
            String s = r.replace("&","§");
            if(s.contains("console")){
                s =s.replace("<player>",p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.split(":")[1]);
            }else if(s.contains("give")){
                s =s.replace("<player>",p.getDisplayName());
                String m = s.split(":")[1];
                String material = m.split("-")[1].toUpperCase(Locale.ROOT).replace(" ","");
                int ammount = Integer.parseInt(m.split("-")[2].replace(" ",""));
                int data = Integer.parseInt(m.split("-")[3].replace(" ",""));

                ItemStack item = new ItemStack(Material.valueOf(material),ammount,(short)data);
                ItemMeta meta = item.getItemMeta();
                if(m.split("-").length>4){
                    meta.setDisplayName(m.split("-")[4].replace("&","§"));
                }
                if(m.split("-").length>5){
                    List<String> lore = new ArrayList<>();
                    for(int i = 5;i<=m.split("-").length-1;i++){
                        lore.add(m.split("-")[i].replace("&","§"));
                    }
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);

                p.getInventory().addItem(item);
            }else if(s.contains("bc")){
                s =s.replace("<player>",p.getDisplayName());
                Bukkit.broadcastMessage(s.split(":")[1].replace("&","§"));
            }else if(s.contains("message")){
                s =s.replace("<player>",p.getDisplayName());
                p.sendMessage(s.split(":")[1].replace("&","§"));
            }
        }
    }
}
