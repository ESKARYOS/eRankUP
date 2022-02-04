package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuListener extends Listeners {

    @EventHandler
    public void event(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem()==null)return;
        if(!e.getCurrentItem().hasItemMeta())return;
        ItemStack item = e.getCurrentItem();
        if(hasRankMenu(e.getView().getTitle())!=null){
            e.setCancelled(true);
            JavaUtils.playSound(p, SoundsAPI.CLICK,1F,1F);
            Rank rank = hasRankMenu(e.getView().getTitle());
            Menu menu = rank.getMenu();
            if(menu.getItems().containsKey("evolve") && e.getCurrentItem().equals(getItem(p,menu.getItems().get("evolve")))){
                RankMenu.confirmEvolve(p);
            }
            if(menu.getItems().containsKey("back") && e.getCurrentItem().equals(getItem(p,menu.getItems().get("back")))){
                RankMenu.rankMenu(p,1);
                return;
            }
            if(menu.getItems().containsKey("close") && e.getCurrentItem().equals(getItem(p,menu.getItems().get("close")))){
                p.closeInventory();
                return;
            }
            return;
        }
        if(RankMenu.menuTitles.contains(e.getView().getTitle())){
            JavaUtils.playSound(p, SoundsAPI.CLICK,1F,1F);
            e.setCancelled(true);
            if(getMenu(e.getView().getTitle())!=null){
                Menu menu = getMenu(e.getView().getTitle());
                if(menu.getItems().containsKey("close")){
                    if(e.getCurrentItem().equals(menu.getItems().get("close"))){
                        p.closeInventory();
                        return;
                    }
                }
            }
            if(e.getView().getTitle().equals(RankMenu.confirmName)){
                if(item.equals(RankMenu.menus.get("ConfirmMenu").clone(p,"confirm"))){
                    RankMain.evolve(p.getUniqueId(),DataMain.getProfile(p.getUniqueId()).getNext().clone());
                    p.closeInventory();
                }
                if(item.equals(RankMenu.menus.get("ConfirmMenu").clone(p,"deny"))){
                    p.closeInventory();
                }
                return;
            }
            if(getMenu(e.getView().getTitle())!=null){
                Menu menu = getMenu(e.getView().getTitle());
                Rank playerRank = DataMain.getProfile(p.getUniqueId()).getRank();
                if(!menu.getRanks().isEmpty()){
                    Rank rank = getRankByDisplay(menu,e.getCurrentItem().getItemMeta().getDisplayName(),p);
                    if(rank.getOrder() <= playerRank.getOrder())return;
                    RankMenu.menu(rank,p);
                }
            }
        }

    }

    public ItemStack getItem(Player p,ItemStack item){
        ItemStack i = new ItemStack(item.getType(),item.getAmount(),item.getDurability());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(RankHolder.hook(p,item.getItemMeta().getDisplayName()));
        List<String> lore = new ArrayList<>();
        for(String k :item.getItemMeta().getLore()){lore.add(RankHolder.hook(p,k));}
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public Menu getMenu(String title){
        for(Menu menus : RankMenu.menus.values()){
            if(menus.getTitle().equals(title)){
                return menus;
            }
        }
        return null;
    }
    public Rank hasRankMenu(String name){
        for(Rank rank : RankMain.getAllRanks()){
            if(rank.getDisplay().equals(name)){
                return rank;
            }
        }
        return null;
    }
    public Rank getRankByDisplay(Menu menu,String display,Player p){
        for(String s : menu.getRanks()){
            String item1 = RankMain.getRankByName(s.split(":")[0]).getRankIcon().getItemMeta().getDisplayName();
            String item2 = RankMain.getRankByName(s.split(":")[0]).getRankIconCompleted().getItemMeta().getDisplayName();

            if(item1.equals(RankHolder.hook(p,display)) || item2.equals(RankHolder.hook(p,display))){
                return RankMain.getRankByName(s.split(":")[0]);
            }
        }
        return null;
    }
    public String getDisplay(ItemStack item,Player p) {
        return RankHolder.hook(p, item.getItemMeta().getDisplayName());
    }

}
