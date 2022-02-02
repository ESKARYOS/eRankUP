package br.com.eskaryos.rankup.utils;

import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemUtils {

    public static ItemStack getItem(YamlConfiguration config,String key){
        Material material = Material.valueOf(config.getString(key+".material").toUpperCase(Locale.ROOT));
        int ammount = config.getInt(key+".ammount");
        int data = config.getInt(key+".data");
        String display = config.getString(key+".display").replace("&","§");
        List<String> lore = new ArrayList<>();
        for(String s : config.getStringList(key+".lore")){
            lore.add(s.replace("&","§"));
        }
        ItemStack item = new ItemStack(material,ammount,(short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static boolean hasItem(Player p, ItemStack item, int ammount) {
        int i = 0;
        ItemStack[] var5 = p.getInventory().getContents();
        int var6 = var5.length;

        for (ItemStack items : var5) {
            if (items != null && items.getType()
                    .equals(item.getType()) && items.getDurability() == item.getDurability()) {
                i += items.getAmount();
            }
        }

        return i >= ammount;
    }

    public static void removeItem(Player p, ItemStack item, int ammount) {
        int slot = 0;
        ItemStack i = new ItemStack(item.getType(), 0, item.getDurability());
        ItemStack[] var5 = p.getInventory().getContents();
        int var6 = var5.length;

        for (ItemStack slotItem : var5) {
            if (slotItem != null && slotItem.getType().equals(item.getType())) {
                i.setAmount(i.getAmount() + slotItem.getAmount());
                p.getInventory().remove(slotItem);
            }

            ++slot;
        }

        i.setAmount(i.getAmount() - ammount);
        if (i.getAmount() > 0) {
            p.getInventory().addItem(i);
        }
    }
}
