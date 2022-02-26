package br.com.eskaryos.rankup.ranks;


import br.com.eskaryos.rankup.menu.Menu;
import br.com.eskaryos.rankup.requirements.Requirement;
import br.com.eskaryos.rankup.requirements.RequirementType;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.nms.ActionBar;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter@Setter
public class Rank {

    private String name;
    private String display;
    private int order;

    private Sound evolveSound;
    private Sound evolveSoundAll;
    private Sound evolveSoundError;

    private List<String> evolveMessage = new ArrayList<>();
    private List<String> evolveMessageAll = new ArrayList<>();

    private String evolveTitle;
    private String evolveSubTitle;
    private String evolveActionbar;
    private String evolveActionbarAll;

    private List<String> commands = new ArrayList<>();

    private Map<String,Requirement> requirements;
    private Map<RequirementType,List<Requirement>> requirementsbytype;
    private Requirement lastRequirement;
    private ItemStack rankIcon;
    private ItemStack rankIconCompleted;

    private Menu menu;

    public Rank(String name,String display,int order){
        this.name = name;
        this.display = display;
        this.order = order;
        requirements = new HashMap<>();
        requirementsbytype = new HashMap<>();
        for (RequirementType value : RequirementType.values()) {
            getRequirementsbytype().put(value,new ArrayList<>());
        }
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
        ActionBar.sendTitle(p,RankHolder.hook(p,evolveTitle),RankHolder.hook(p,evolveSubTitle),0,20*5,0);
    }

    public int getTotalMax(){
        int value = 0;
        for (Requirement requirement : getRequirements().values()) {
            value = value +requirement.getMax();
        }
        return value;
    }
    public int getTotalValue(){
        int value = 0;
        for (Requirement requirement : getRequirements().values()) {
            value = value +requirement.getValue();
        }
        return value;
    }

    /**
     * Method to clone rank
     */
    public Rank clone() {
        Rank r = new Rank(getName(),getDisplay(),getOrder());
        r.setEvolveSound(getEvolveSound());
        r.setEvolveSoundAll(getEvolveSoundAll());
        r.setEvolveSoundError(getEvolveSoundError());

        r.setEvolveMessage(new ArrayList<>(getEvolveMessage()));
        r.setEvolveMessageAll(new ArrayList<>(getEvolveMessageAll()));

        r.setRankIcon(getRankIcon().clone());
        r.setRankIconCompleted(getRankIconCompleted().clone());
        r.setCommands(new ArrayList<>(getCommands()));

        r.setEvolveActionbar(getEvolveActionbar());
        r.setEvolveActionbarAll(getEvolveActionbarAll());

        r.setEvolveTitle(getEvolveTitle());
        r.setEvolveSubTitle(getEvolveSubTitle());
        for (String s : getRequirements().keySet()) {
            Requirement clone = cloneRequirement(getRequirements().get(s));
            r.getRequirements().put(s,clone);
            r.getRequirementsbytype().get(clone.getType()).add(clone);
        }
        r.setMenu(getMenu());

        return r;
    }

    public Requirement cloneRequirement(Requirement r){
        RequirementType type = r.getType();
        String name = r.getName();
        int max = r.getMax();
        List<String> cMessage = r.getCompletedMessage();
        String cTitle = r.getTitleCompleted();
        String cBar = r.getActionBarCompleted();
        SoundsAPI sound = r.getSoundCompleted();
        Requirement requirement = new Requirement(name, type, max, cMessage, cTitle, cBar, sound);
        if(r.getItem()!=null){requirement.setItem(r.getItem().clone());}
        if(r.getEntity()!=null){requirement.setEntity(r.getEntity()+"");}
        if(r.getEnchantment()!=null){requirement.setEnchantment(r.getEnchantment());}
        return requirement;
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
     * @param p Player to send Message
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
        if(getEvolveSoundAll()==null)return;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(), evolveSoundAll,1F,1F);
        }
    }
    /**
     * Method to send sound evolve
     * @param p Player to send Sound
     */
    public void sendEvolveSound(Player p){
        if(getEvolveSound()==null)return;
        p.playSound(p.getLocation(), evolveSound,1F,1F);
    }

    /**
     * Method to send sound error
     * @param p Player to send Message
     */
    public void sendEvolveSoundError(Player p){
        if(getEvolveSound()==null)return;
        p.playSound(p.getLocation(), evolveSoundError,1F,1F);
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
                Material material = Material.matchMaterial(m.split("-")[1].toUpperCase(Locale.ROOT).replace(" ",""));
                int ammount = Integer.parseInt(m.split("-")[2].replace(" ",""));
                int data = Integer.parseInt(m.split("-")[3].replace(" ",""));

                assert material != null;
                ItemStack item = new ItemStack(material,ammount,(short)data);
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
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
