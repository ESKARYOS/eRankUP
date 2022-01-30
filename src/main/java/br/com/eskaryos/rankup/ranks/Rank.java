package br.com.eskaryos.rankup.ranks;


import br.com.eskaryos.rankup.utils.api.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

@Getter@Setter
public class Rank {

    private String name;
    private String display;
    private int order;

    private SoundsAPI evolveSound;
    private SoundsAPI evolveSoundAll;
    private SoundsAPI evolveSoundError;


    private List<String> evolveMessage;
    private List<String> evolveMessageAll;

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
}
