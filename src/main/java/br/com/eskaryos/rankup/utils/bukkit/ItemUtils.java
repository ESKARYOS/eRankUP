package br.com.eskaryos.rankup.utils.bukkit;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemUtils {


    public static ItemStack getItem(YamlConfiguration config,String key){
        ItemStack item;
        String display = Objects.requireNonNull(config.getString(key + ".display")).replace("&", "§");
        List<String> lore = new ArrayList<>();
        for (String s : config.getStringList(key + ".lore")) {
            lore.add(s.replace("&", "§"));
        }
        if(Objects.requireNonNull(config.getString(key + ".material")).contains("head")){
            String texture = Objects.requireNonNull(config.getString(key + ".material")).split(":")[1];
            item = JavaUtils.getConfigItem(texture);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(display);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }else{
            Material material = Material.getMaterial(Objects.requireNonNull(config.getString(key + ".material")).toUpperCase(Locale.ROOT));
            int ammount = config.getInt(key + ".ammount");
            int data = config.getInt(key + ".data");
            assert material != null;
            item = new ItemStack(material, ammount, (short) data);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(display);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
    public static boolean hasItem(Player p, ItemStack item, int ammount) {
        int i = 0;
        ItemStack[] var5 = p.getInventory().getContents();

        for (ItemStack items : var5) {
            if (items.getDurability() == item.getDurability())
                if (items.getType().equals(item.getType())) {
                    i += items.getAmount();
                }
        }

        return i >= ammount;
    }

    public static void removeItem(Player p, ItemStack item, int ammount) {
        ItemStack i = new ItemStack(item.getType(), 0, item.getDurability());
        ItemStack[] var5 = p.getInventory().getContents();

        for (ItemStack slotItem : var5) {
            if (slotItem != null && slotItem.getType().equals(item.getType())) {
                i.setAmount(i.getAmount() + slotItem.getAmount());
                p.getInventory().remove(slotItem);
            }

        }

        i.setAmount(i.getAmount() - ammount);
        if (i.getAmount() > 0) {
            p.getInventory().addItem(i);
        }
    }
}
