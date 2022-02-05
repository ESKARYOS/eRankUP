package br.com.eskaryos.rankup.menu;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.utils.bukkit.ItemUtils;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

public class RankMenu {

    public static Map<String,Menu> menus = new HashMap<>();

    public static String confirmName;
    private static int confirmSize;

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
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        confirmName = convert(Objects.requireNonNull(config.getString("Confirm-Menu.name")));
        confirmSize = config.getInt("Confirm-Menu.size");

        confirmItem = getItem(config,"Confirm-Menu.items.confirm");
        denyItem = getItem(config,"Confirm-Menu.items.deny");

        menuTitles.add(confirmName);

        Menu confirmMenu = new Menu(confirmName,confirmSize);
        for(String key : config.getConfigurationSection("Confirm-Menu.items").getKeys(false)){
            confirmMenu.getItems().put(key, ItemUtils.getItem(config,"Confirm-Menu.items."+key));
            confirmMenu.getItemSlot().put(key,config.getInt("Confirm-Menu.items."+key+".slot"));
        }

        for(String key : config.getConfigurationSection("Rank-Menu").getKeys(false)){
            String name = convert(config.getString("Rank-Menu."+key+".name"));
            int slot = config.getInt("Rank-Menu."+key+".size");
            Menu menu = new Menu(name,slot);
            menuTitles.add(name);
            for(String key2 : config.getConfigurationSection("Rank-Menu."+key+".items").getKeys(false)){
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





    private static String convert(String l){
        return ChatColor.translateAlternateColorCodes('&',l);
    }
    private static List<String> convert(List<String> l){
        List<String> list = new ArrayList<>();
        for(String l2: l){list.add(ChatColor.translateAlternateColorCodes('&',l2));}
        return list;
    }

    public static ItemStack getItem(YamlConfiguration config, String key){
        ItemStack item = null;
        if(config.getString(key+".material").contains("head")){
            item = JavaUtils.getConfigItem(config.getString(key+".material").split(":")[1]);

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if(config.contains(key+".display")){
                String name = convert(Objects.requireNonNull(config.getString(key+".display")));
                meta.setDisplayName(name);
            }
            if(config.contains(key+".lore")){
                List<String> lore = convert(config.getStringList(key+".lore"));
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            return item;
        }

        Material material = Material.matchMaterial(Objects.requireNonNull(config.getString(key + ".material")).toUpperCase(Locale.ROOT));
        int ammount = config.getInt(key + ".ammount");
        int data = config.getInt(key + ".data");
        item = new ItemStack(material, ammount, (short) data);

        ItemMeta meta = item.getItemMeta();
        if(config.contains(key+".display")){
            String name = convert(Objects.requireNonNull(config.getString(key+".display")));
            meta.setDisplayName(name);
        }
        if(config.contains(key+".lore")){
            List<String> lore = convert(config.getStringList(key+".lore"));
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}
