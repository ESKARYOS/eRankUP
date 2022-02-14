package br.com.eskaryos.rankup.requirements;

import br.com.eskaryos.rankup.utils.nms.ActionBar;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Requirement {

    private List<String> completedMessage;
    private SoundsAPI soundCompleted;
    private String titleCompleted;
    private String actionBarCompleted;

    private ItemStack item;
    private String entity;
    private int value = 0;
    private int max;

    public Requirement (ItemStack item,String entity,int max,List<String> completedMessage,String titleCompleted,String actionBarCompleted,SoundsAPI soundCompleted){
        this.item = item;
        this.entity = entity;
        this.max = max;
        this.completedMessage = completedMessage;
        this.titleCompleted = titleCompleted;
        this.actionBarCompleted = actionBarCompleted;
        this.soundCompleted = soundCompleted;
    }

    public void sendBar(Player p){
        if(actionBarCompleted.isEmpty())return;
        ActionBar.sendActionBar(p, RankHolder.hook(p,actionBarCompleted));
    }
    public void sendTitle(Player p){
        if(titleCompleted.isEmpty())return;
        String title = titleCompleted.split(":")[0];
        String sub = titleCompleted.split(":")[1];
        ActionBar.sendTitle(p,RankHolder.hook(p,title),RankHolder.hook(p,sub),0,20*4,0);
    }
    public void sendSound(Player p){
        if(soundCompleted ==null)return;
        p.playSound(p.getLocation(), Objects.requireNonNull(soundCompleted.bukkitSound()),1F,1F);
    }

}
