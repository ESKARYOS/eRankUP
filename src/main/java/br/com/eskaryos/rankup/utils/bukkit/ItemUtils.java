package br.com.eskaryos.rankup.utils.bukkit;

import br.com.eskaryos.rankup.data.Lang;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemUtils extends Utils{


    public static ItemStack getItem(YamlConfiguration config,String key){
        ItemStack item;
        String display = Objects.requireNonNull(color(config.getString(key + ".display")));
        List<String> lore = color(config.getStringList(key + ".lore"));
        if(Objects.requireNonNull(config.getString(key + ".material")).contains("head")) {
            String texture = Objects.requireNonNull(config.getString(key + ".material")).split(":")[1];
            item = JavaUtils.getConfigItem(texture);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(display);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }else if(Objects.requireNonNull(config.getString(key + ".material")).contains("banner")) {
            item = Lang.banners.get(Objects.requireNonNull(config.getString(key + ".material")).split(":")[1]).getBanner();
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(display);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }else{
            Material material = Material.matchMaterial(Objects.requireNonNull(config.getString(key + ".material")).toUpperCase(Locale.ROOT));
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
}
