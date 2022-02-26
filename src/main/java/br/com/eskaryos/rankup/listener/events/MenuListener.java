package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.menu.Menu;
import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MenuListener extends Listeners {

    @EventHandler
    public void event(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem()==null)return;
        if(!e.getCurrentItem().hasItemMeta())return;
        ItemStack item = e.getCurrentItem();
        List<String> menuTitles = RankMenu.menuTitles;
        String title = e.getView().getTitle();
        if(hasConfirmMenu(e)){return;}
        if(hasRankMenu(e)){return;}
        if(menuTitles.contains(title)){
            JavaUtils.playSound(p, Utils.getSound("CLICK"),1F,1F);
            e.setCancelled(true);
            Menu menu = getMenu(e.getView().getTitle());
            if(menu==null)return;
            int page = menu.getPage();
            if(menu.getItems().containsKey("close")){if(hasSimilar(p,item,getItem(p,menu.getItems().get("close")))){p.closeInventory();return;}}

            if(menu.getItems().containsKey("next")){if(hasSimilar(p,item,getItem(p,menu.getItems().get("next")))){
                    page = page+1;
                    if(!RankMenu.menus.containsKey("page-"+page))return;
                    RankMenu.rankMenu(p,page);
                    return;}
            }
            if(menu.getItems().containsKey("back")){if(hasSimilar(p,item,getItem(p,menu.getItems().get("back")))){
                    page = menu.getPage()-1;
                    if(!RankMenu.menus.containsKey("page-"+page))return;
                    RankMenu.rankMenu(p,page);
                    return;}
            }

            Rank playerRank = DataMain.getProfile(p.getUniqueId()).getRank();if(!menu.getRanks().isEmpty()){
                Rank rank = getRankByDisplay(menu, Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName(),p);
                if(rank.getOrder() <= playerRank.getOrder())return;
                RankMenu.menu(rank,p);}
        }

    }

    public boolean hasRankMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        assert item != null;
        if(hasRankMenu(e.getView().getTitle())!=null){
            JavaUtils.playSound(p, Utils.getSound("CLICK"),1F,1F);
            Rank rank = hasRankMenu(e.getView().getTitle());
            Menu menu = rank.getMenu();
            if(menu.getItems().containsKey("evolve") && hasSimilar(p,item,getItem(p,menu.getItems().get("evolve")))){
                RankMenu.confirmEvolve(p);
                e.setCancelled(true);
                return true;
            }
            if(menu.getItems().containsKey("back") && hasSimilar(p,item,getItem(p,menu.getItems().get("back")))){
                RankMenu.rankMenu(p,1);
                e.setCancelled(true);
                return true;
            }
            if(menu.getItems().containsKey("close")) {
                if (hasSimilar(p,item,getItem(p,menu.getItems().get("close")))) {
                    p.closeInventory();
                    e.setCancelled(true);
                    return true;
                }
            }
            e.setCancelled(true);
            return true;
        }
        return false;
    }

    public boolean hasConfirmMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if(e.getView().getTitle().equals(RankMenu.confirmName)){
            e.setCancelled(true);
            assert item != null;
            Rank next = DataMain.getProfile(p.getUniqueId()).getNext().clone();
            if(hasSimilar(p,item,RankMenu.menus.get("ConfirmMenu").clone(p,"confirm"))){
                RankMain.evolve(p.getUniqueId(),next);
                p.closeInventory();
                return true;
            }
            if(hasSimilar(p,item,RankMenu.menus.get("ConfirmMenu").clone(p,"deny"))){
                p.closeInventory();
                JavaUtils.playSound(p, next.getEvolveSoundError(),1F,1F);
                return true;
            }
            return true;
        }
        return false;
    }

    public ItemStack getItem(Player p,ItemStack item){
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        meta.setDisplayName(RankHolder.hook(p, Objects.requireNonNull(item.getItemMeta()).getDisplayName()));
        List<String> lore = new ArrayList<>();
        for(String k : Objects.requireNonNull(item.getItemMeta().getLore())){lore.add(RankHolder.hook(p,k));}
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public Menu getMenu(String title){
        List<Menu> list = new ArrayList<>(RankMenu.menus.values());
        Collections.reverse(list);
        for(Menu menus : list){
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
            String item1 = Objects.requireNonNull(RankMain.getRankByName(s.split(":")[0]).getRankIcon().getItemMeta()).getDisplayName();
            String item2 = Objects.requireNonNull(RankMain.getRankByName(s.split(":")[0]).getRankIconCompleted().getItemMeta()).getDisplayName();

            if(item1.equals(RankHolder.hook(p,display)) || item2.equals(RankHolder.hook(p,display))){
                return RankMain.getRankByName(s.split(":")[0]);
            }
        }
        return null;
    }
    public boolean hasSimilar(Player p,ItemStack item,ItemStack item2){
        if(item.getType()!=item2.getType())return false;
        String display = RankHolder.hook(p, Utils.color(Objects.requireNonNull(item2.getItemMeta()).getDisplayName()));
        return Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals(display);
    }

}
