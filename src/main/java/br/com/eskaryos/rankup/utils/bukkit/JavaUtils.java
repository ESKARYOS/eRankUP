package br.com.eskaryos.rankup.utils.bukkit;

import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.utils.api.EnchantAPI;
import br.com.eskaryos.rankup.utils.api.SkullCreator;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import com.google.common.io.Files;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }

        file.delete();
    }
    public static ItemStack getConfigItem(String line) {
        return SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/" + line);
    }
    public static <T> List<T> misturar(List<T> lista) {
        List<T> sorted = new ArrayList<>(lista);
        Collections.shuffle(sorted);
        return sorted;
    }

    public static ItemStack loadItem(ItemStack item2, String line) {
        ItemMeta mitem = item2.getItemMeta();
        mitem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (line.length() > 2) {
            for (int i = 3; i < (line.split(":")).length; i++) {
                int lvl = Integer.parseInt(line.split(":")[i + 1]);
                mitem.addEnchant(Objects.requireNonNull(EnchantAPI.valueOf(line.split(":")[i]).getEnchant()), lvl,
                        true);
                i++;
            }
        }
        item2.setItemMeta(mitem);
        return item2;
    }

    public static void clearPlayer(Player p) {
        p.spigot().setCollidesWithEntities(true);
        p.setLevel(0);
        p.setTotalExperience(0);
        p.setFoodLevel(20);
        p.setExp(0);
        p.setExhaustion(0);
        p.getInventory().clear();
        p.setItemOnCursor(new ItemStack(Material.AIR));
        p.getInventory().setArmorContents(new ItemStack[4]);
        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
    }

    public static String convertTime(int tempo) {
        if(tempo<60){
            return tempo+"s";
        }
        int mins = tempo / 60;
        int remainderSecs = tempo - mins * 60;
        if (mins < 60)
            return ((mins < 10) ? "0" : "") + mins + ":" + ((remainderSecs < 10) ? "0" : "") + remainderSecs + "";
        int hours = mins / 60;
        int remainderMins = mins - hours * 60;
        return ((hours < 10) ? "0" : "") + hours + ":" + ((remainderMins < 10) ? "0" : "") + remainderMins + ":" + ((remainderSecs < 10) ? "0" : "") + remainderSecs + "";
    }
    public static boolean isInArea(Location l, Location l1, Location l2){

        double x1 = Math.min(l1.getX(), l2.getX());
        double y1 = Math.min(l1.getY(), l2.getY());
        double z1 = Math.min(l1.getZ(), l2.getZ());

        double x2 = Math.max(l1.getX(), l2.getX());
        double y2 = Math.max(l1.getY(), l2.getY());
        double z2 = Math.max(l1.getZ(), l2.getZ());

        if(l.getX() >= x1 && l.getX() <= x2 &&
                l.getY() >= y1 && l.getY() <= y2 &&
                l.getZ() >= z1 && l.getZ() <= z2){
            return true;
        }
        return false;
    }
    public static List<Location> getBlocksBetweenPoints(Location l1, Location l2,boolean random,boolean getAir) {
        List<Location> blocks = new ArrayList<Location>();
        int topBlockX = (Math.max(l1.getBlockX(), l2.getBlockX()));
        int bottomBlockX = (Math.min(l1.getBlockX(), l2.getBlockX()));
        int topBlockY = (Math.max(l1.getBlockY(), l2.getBlockY()));
        int bottomBlockY = (Math.min(l1.getBlockY(), l2.getBlockY()));
        int topBlockZ = (Math.max(l1.getBlockZ(), l2.getBlockZ()));
        int bottomBlockZ = (Math.min(l1.getBlockZ(), l2.getBlockZ()));
        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int y = bottomBlockY; y <= topBlockY; y++) {
                for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Location block = new Location(l1.getWorld(),x,y,z);
                    int r = new Random().nextInt(100);
                    if(getAir){
                        if(random){
                            if(r<=5){blocks.add(block);}
                        }else{
                            blocks.add(block);
                        }
                    }else{
                        if(!block.getBlock().getType().equals(Material.AIR)){
                            if(random){
                                if(r<=5){blocks.add(block);}
                            }else{
                                blocks.add(block);
                            }
                        }
                    }
                }
            }
        }
        return blocks;

    }
    public static String makeProgressBar(double currentExp, double needExp, String barraPreenchida, String barraFundo, double divisorias) {
        StringBuilder progressBar = new StringBuilder();
        double percentage = currentExp >= needExp ? divisorias : ((currentExp * divisorias) / needExp);

        boolean higher = false, hasColor = false;
        for (double d = 1.0; d <= divisorias; d += 1.0) {
            if (!higher && percentage >= d) {
                progressBar.append(barraPreenchida);
                higher = true;
                hasColor = true;
            } else if ((higher || !hasColor) && percentage < d) {
                higher = false;
                hasColor = true;
                progressBar.append(barraFundo);
            }

            progressBar.append(Lang.bar);
        }

        return progressBar.toString();
    }
    public static String convert(double value) {
        List<String> suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "L");
        int index = 0;
        double tmp;
        if (value >= 1000000.0) {
            while ((tmp = value / 1000) >= 1) {
                value = tmp;
                ++index;
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        return decimalFormat.format(value) + suffixes.get(index);
    }

    public static ItemStack add(Material material, int ammount, int data) {
        return new ItemStack(material, ammount, (short) data);
    }

    public static ItemStack add(Material material, int ammount, int data, String nome) {
        ItemStack item = new ItemStack(material, ammount, (short) data);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(nome);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack add(Material material, int ammount, int data, String nome, List<String> lore) {
        ItemStack item = new ItemStack(material, ammount, (short) data);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(nome);
        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    public static boolean hasItem(Player p, ItemStack item, int ammount) {
        int i = 0;
        for (ItemStack items : p.getInventory().getContents()) {
            if (items != null && items.getType()
                    .equals(item.getType()) && items.getDurability() == item.getDurability()) {
                i += items.getAmount();
            }
        }
        return i >= ammount;
    }

    public static void playSound(Player p, SoundsAPI sound, float f1, float f2) {
        p.playSound(p.getLocation(), sound.bukkitSound(), f1, f2);
    }
    public static void sendAllMessage(String message){
        for(Player p : Bukkit.getOnlinePlayers()){p.sendMessage(message);}
    }
    public static void sendAllSound(SoundsAPI s, float f1,float f2){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(),s.bukkitSound(),f1,f2);
        }
    }

    public static int hasItem(Player p, ItemStack item) {
        int i = 0;
        for (ItemStack items : p.getInventory().getContents()) {
            if (items != null && items.getType()
                    .equals(item.getType()) && items.getDurability() == item.getDurability()) {
                i += items.getAmount();
            }
        }
        return i;
    }

    public static YamlConfiguration loadConfigUTF8(File file) {
        YamlConfiguration fileConfiguration = new YamlConfiguration();
        try {
            file.createNewFile();
            fileConfiguration.loadFromString(Files.toString(file, StandardCharsets.UTF_8));
        } catch (Exception e) {
        }

        return fileConfiguration;
    }


    public static List<String> getLore(List<String> lista) {
        List<String> s = new ArrayList<>();

        for (String ss : lista) {
            s.add(ss.replace("&", "§"));
        }

        return s;
    }
}
