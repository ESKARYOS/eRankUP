package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter@Setter
public class Menu extends Utils {

    private Map<String, ItemStack> items = new HashMap<>();

    private List<String> ranks = new ArrayList<>();

    private Map<String, Integer> itemSlot = new HashMap<>();
    private String title;
    private int slots;
    int page;

    public Menu(String title,int slots,int page){
        this.title = title;
        this.slots = slots;
        this.page = page;
    }

    public Inventory getMenu(Player p,boolean hasRankMenu){
        Inventory inv = Bukkit.createInventory(p,getSlots(),getTitle());
        Map<String,ItemStack> list = fixItems(p);

        for(String key: getItemSlot().keySet()){
            if(hasRankMenu){
                inv.setItem(getItemSlot().get(key),list.get(key));
            }else{
                ItemStack item = list.get(key);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(new ArrayList<>());
                item.setItemMeta(meta);
                inv.setItem(getItemSlot().get(key),list.get(key));
            }
        }
        int order = DataMain.getProfile(p.getUniqueId()).getRank().getOrder();
        for(String name : getRanks()){
            Rank rank = RankMain.getRankByName(name.split(":")[0]);
            int slot = Integer.parseInt(name.split(":")[1]);
            if(order>=rank.getOrder()){
                ItemStack clone = clone(p,rank.getRankIconCompleted());
                glow(clone);
                inv.setItem(slot,clone(p,clone));
            }else{
                if(rank.getOrder()==DataMain.getProfile(p.getUniqueId()).getNext().getOrder()){
                    inv.setItem(slot,clone(p,rank.getRankIcon()));
                }else{
                    ItemStack item = clone(p,rank.getRankIcon());
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(new ArrayList<>());
                    item.setItemMeta(meta);
                    inv.setItem(slot,item);
                }
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
        ItemStack nItem = item.clone();
        if(item.hasItemMeta()){
            ItemMeta meta = nItem.getItemMeta();
            assert meta != null;
            if(Objects.requireNonNull(item.getItemMeta()).hasDisplayName()){
                meta.setDisplayName(RankHolder.hook(p, color(item.getItemMeta().getDisplayName())));
            }
            if(item.getItemMeta().hasLore()){
                List<String> lore = new ArrayList<>();
                for(String s : Objects.requireNonNull(item.getItemMeta().getLore())){lore.add(RankHolder.hook(p,s));}
                meta.setLore(lore);
            }

            nItem.setItemMeta(meta);
        }
        return nItem;
    }

    public ItemStack clone(Player p,ItemStack item){
        ItemStack nItem = item.clone();
        if(item.hasItemMeta()){
            ItemMeta meta = nItem.getItemMeta();
            assert meta != null;
            if(Objects.requireNonNull(item.getItemMeta()).hasDisplayName()){
                meta.setDisplayName(RankHolder.hook(p,color(item.getItemMeta().getDisplayName())));
            }
            if(item.getItemMeta().hasLore()){
                List<String> lore = new ArrayList<>();
                for(String s : Objects.requireNonNull(item.getItemMeta().getLore())){
                    lore.add(RankHolder.hook(p,s));
                }
                meta.setLore(lore);
            }

            nItem.setItemMeta(meta);
        }
        return nItem;
    }

}
