package br.com.eskaryos.rankup.ranks;


import br.com.eskaryos.rankup.utils.api.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    private List<String> commands = new ArrayList<>();

    private ItemStack rankIcon;
    private ItemStack rankIconCompleted;

    public Rank(String name,String display,int order){
        this.name = name;
        this.display = display;
        this.order = order;
    }
    public Rank clone(){
        Rank r = new Rank(getName(),getDisplay(),getOrder());
        r.setEvolveSound(getEvolveSound());
        r.setEvolveSoundAll(getEvolveSoundAll());
        r.setEvolveSoundError(getEvolveSoundError());
        r.setEvolveMessage(getEvolveMessage());
        r.setEvolveMessageAll(getEvolveMessageAll());
        r.setRankIcon(getRankIcon().clone());
        r.setRankIconCompleted(getRankIconCompleted().clone());
        return r;
    }

    public void sendAllEvolveMessage(Player p){
        for(Player g : Bukkit.getOnlinePlayers()){
            for(String message : evolveMessageAll){
                if(g!=p){
                    g.sendMessage(RankHolder.hook(p,message));
                }
            }
        }
    }
    public void sendEvolveMessage(Player p){
        for(String message : evolveMessage){
            p.sendMessage(RankHolder.hook(p,message));
        }
    }

    public void sendAllEvolveSound(){
        if(getEvolveSoundAll()==SoundsAPI.DISABLED)return;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSoundAll().bukkitSound()),1F,1F);
        }
    }
    public void sendEvolveSound(Player p){
        if(getEvolveSound()==SoundsAPI.DISABLED)return;
        p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSound().bukkitSound()),1F,1F);
    }
    public void sendEvolveSoundError(Player p){
        if(getEvolveSoundError()==SoundsAPI.DISABLED)return;
        p.playSound(p.getLocation(), Objects.requireNonNull(getEvolveSoundError().bukkitSound()),1F,1F);
    }

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
