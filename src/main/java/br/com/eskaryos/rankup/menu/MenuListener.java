package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.api.RankHolder;
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

        if(RankMenu.menuTitles.contains(e.getView().getTitle())){
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
                    Rank rank = DataMain.getProfile(p.getUniqueId()).getRank();

                    RankMain.evolve(p.getUniqueId(),RankMain.getRankById(rank.getOrder()+1));p.closeInventory();
                }
                if(item.equals(RankMenu.menus.get("ConfirmMenu").clone(p,"deny"))){
                    p.closeInventory();
                }
                return;
            }
            if(getMenu(e.getView().getTitle())!=null){
                Menu menu = getMenu(e.getView().getTitle());
                if(!menu.getRanks().isEmpty()){
                    if(getRankByDisplay(menu,e.getCurrentItem().getItemMeta().getDisplayName(),p)!=null){
                        Rank rank = getRankByDisplay(menu,e.getCurrentItem().getItemMeta().getDisplayName(),p);
                        RankMain.evolve(p.getUniqueId(),rank);
                    }
                }
            }
        }

    }

    public Menu getMenu(String title){
        for(Menu menus : RankMenu.menus.values()){
            if(menus.getTitle().equals(title)){
                return menus;
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
