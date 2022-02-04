package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.placeholder.RankHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter@Setter
public class Menu {

    private Map<String, ItemStack> items = new HashMap<>();

    private List<String> ranks = new ArrayList<>();

    private Map<String, Integer> itemSlot = new HashMap<>();
    private String title;
    private int slots;

    public Menu(String title,int slots){
        this.title = title;
        this.slots = slots;
    }

    public Inventory getMenu(Player p){
        Inventory inv = Bukkit.createInventory(p,getSlots(),getTitle());
        Map<String,ItemStack> list = fixItems(p);
        for(String key: getItemSlot().keySet()){
            inv.setItem(getItemSlot().get(key),list.get(key));
        }
        int order = DataMain.getProfile(p.getUniqueId()).getRank().getOrder();
        for(String name : getRanks()){
            Rank rank = RankMain.getRankByName(name.split(":")[0]);
            int slot = Integer.parseInt(name.split(":")[1]);
            if(order>=rank.getOrder()){
                inv.setItem(slot,clone(p,rank.getRankIconCompleted()));
            }else{
                inv.setItem(slot,clone(p,rank.getRankIcon()));
            }
        }

        return inv;
    }


    public Map<String,ItemStack> fixItems(Player p){
        Map<String,ItemStack> items = new HashMap<>();
        for(String key : getItems().keySet()){
            items.put(key,clone(p,getItems().get(key)));
        }
        return items;
    }

    public ItemStack clone(Player p,String itemNovo){
        ItemStack item = getItems().get(itemNovo);
        ItemStack nItem = new ItemStack(item.getType(),item.getAmount(),item.getDurability());
        if(item.hasItemMeta()){
            ItemMeta meta = nItem.getItemMeta();
            if(item.getItemMeta().hasDisplayName()){
                meta.setDisplayName(RankHolder.hook(p,item.getItemMeta().getDisplayName()));
            }
            if(item.getItemMeta().hasLore()){
                List<String> lore = new ArrayList<>();
                for(String s : item.getItemMeta().getLore()){lore.add(RankHolder.hook(p,s));}
                meta.setLore(lore);
            }

            nItem.setItemMeta(meta);
        }
        return nItem;
    }

    public ItemStack clone(Player p,ItemStack item){
        ItemStack nItem = new ItemStack(item.getType(),item.getAmount(),item.getDurability());
        if(item.hasItemMeta()){
            ItemMeta meta = nItem.getItemMeta();
            if(item.getItemMeta().hasDisplayName()){
                meta.setDisplayName(RankHolder.hook(p,item.getItemMeta().getDisplayName()));
            }
            if(item.getItemMeta().hasLore()){
                List<String> lore = new ArrayList<>();
                for(String s : item.getItemMeta().getLore()){lore.add(RankHolder.hook(p,s));}
                meta.setLore(lore);
            }

            nItem.setItemMeta(meta);
        }
        return nItem;
    }

}
