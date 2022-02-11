package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.utils.bukkit.ColorUtils;
import br.com.eskaryos.rankup.utils.bukkit.ItemUtils;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

public class RankMenu {

    public static Map<String,Menu> menus = new HashMap<>();

    public static String confirmName;

    public static List<String> menuTitles = new ArrayList<>();

    public static ItemStack confirmItem;
    public static ItemStack denyItem;

    public static void confirmEvolve(Player p){
        p.openInventory(menus.get("ConfirmMenu").getMenu(p));
    }

    public static void menu(Rank rank, Player p){
        p.openInventory(rank.getMenu().getMenu(p));
    }

    public static void rankMenu(Player p,int page){
        if(menus.containsKey("page-"+page)){
            p.openInventory(menus.get("page-"+page).getMenu(p));
        }else{
            p.sendMessage(Lang.invalidMenu);
        }
    }

    public static void LoadMenus(){
        File file = new File(Main.plugin.getDataFolder(),"menu.yml");
        if(!file.exists()){Main.plugin.saveResource("menu.yml",true);}
        YamlConfiguration config = JavaUtils.loadConfigUTF8(file);

        confirmName = ColorUtils.translateStringColor(Objects.requireNonNull(config.getString("Confirm-Menu.name")));
        int confirmSize = config.getInt("Confirm-Menu.size");

        confirmItem = getItem(config,"Confirm-Menu.items.confirm");
        denyItem = getItem(config,"Confirm-Menu.items.deny");

        menuTitles.add(confirmName);

        Menu confirmMenu = new Menu(confirmName, confirmSize,1);
        for(String key : Objects.requireNonNull(config.getConfigurationSection("Confirm-Menu.items")).getKeys(false)){
            confirmMenu.getItems().put(key, ItemUtils.getItem(config,"Confirm-Menu.items."+key));
            confirmMenu.getItemSlot().put(key,config.getInt("Confirm-Menu.items."+key+".slot"));
        }
        for(String key : Objects.requireNonNull(config.getConfigurationSection("Rank-Menu")).getKeys(false)){
            String name = ColorUtils.translateStringColor(config.getString("Rank-Menu."+key+".name"));
            int slot = config.getInt("Rank-Menu."+key+".size");
            Menu menu = new Menu(name,slot,Integer.parseInt(key.split("-")[1]));
            menuTitles.add(name);

            for(String key2 : Objects.requireNonNull(config.getConfigurationSection("Rank-Menu." + key + ".items")).getKeys(false)){
                if(config.contains("Rank-Menu."+key+".items."+key2+".rank")){
                    String rank = config.getString("Rank-Menu."+key+".items."+key2+".rank");
                    int slt = config.getInt("Rank-Menu."+key+".items."+key2+".slot");
                    menu.getRanks().add(rank+":"+slt);
                }else{
                    menu.getItems().put(key2,ItemUtils.getItem(config,"Rank-Menu."+key+".items."+key2));
                    menu.getItemSlot().put(key2,config.getInt("Rank-Menu."+key+".items."+key2+".slot"));
                }
            }
            menus.put(key,menu);
        }

        menus.put("ConfirmMenu",confirmMenu);
    }


    public static ItemStack getItem(YamlConfiguration config, String key){
        String name = "";
        List<String> lore = new ArrayList<>();

        if(config.contains(key+".display")){name = ColorUtils.translateStringColor(Objects.requireNonNull(config.getString(key+".display")));}
        if(config.contains(key+".lore")){lore = ColorUtils.translateStringColor(config.getStringList(key+".lore"));}


        if(Objects.requireNonNull(config.getString(key + ".material")).contains("head:")){
            ItemStack item = JavaUtils.getConfigItem(Objects.requireNonNull(config.getString(key + ".material")).split(":")[1]);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(name);meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        if(Objects.requireNonNull(config.getString(key + ".material")).contains("banner:")){
            ItemStack item = Lang.banners.get(Objects.requireNonNull(config.getString(key + ".material")).split(":")[1]).getBanner();
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        Material material = Material.getMaterial(Objects.requireNonNull(config.getString(key + ".material")).toUpperCase());
        int ammount = config.getInt(key + ".ammount");
        int data = config.getInt(key + ".data");
        assert material != null;
        ItemStack item = new ItemStack(material, ammount, (short) data);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
