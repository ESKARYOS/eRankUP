package br.com.eskaryos.rankup.utils.bukkit;

import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import br.com.eskaryos.rankup.utils.nms.NMS;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {


    public static Sound getSound(String sound){
        try {
            return Sound.valueOf(sound);
        }catch (IllegalArgumentException e){
            return SoundsAPI.valueOf(sound).bukkitSound();
        }
    }

    /**
     * Allows to return a material according to its ID Works only for plugins
     * from 1.8 to 1.12
     *
     * @param id
     * @return the material according to his id
     */
    protected Material getMaterial(int id) {
        return byId.length > id && id >= 0 ? byId[id] : Material.AIR;
    }


    /**
     * Allows you to check if the inventory is full
     *
     * @param player
     * @return true if the player's inventory is full
     */
    protected boolean hasInventoryFull(Player player) {
        int slot = 0;
        PlayerInventory inventory = player.getInventory();
        for (int a = 0; a != 36; a++) {
            ItemStack itemStack = inventory.getContents()[a];
            if (itemStack == null)
                slot++;
        }
        return slot == 0;
    }


    /**
     * Gives an item to the player, if the player's inventory is full then the
     * item will drop to the ground
     *
     * @param player
     * @param item
     */

    /**
     *
     * @param message
     * @return
     */
    public static String color(String message) {
        if (message == null)
            return null;
        if (NMS.isHexColor()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = pattern.matcher(message);
            }
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     *
     * @param message
     * @return
     */
    protected String colorReverse(String message) {
        return message == null ? null : message.replace("§", "&");
    }

    /**
     *
     * @param messages
     * @return
     */
    protected static List<String> color(List<String> messages) {
        return messages.stream().map(message -> color(message)).collect(Collectors.toList());
    }

    /**
     *
     * @param messages
     * @return
     */
    protected List<String> colorReverse(List<String> messages) {
        return messages.stream().map(message -> colorReverse(message)).collect(Collectors.toList());
    }

    /**
     *
     * @param flagString
     * @return
     */
    protected ItemFlag getFlag(String flagString) {
        for (ItemFlag flag : ItemFlag.values()) {
            if (flag.name().equalsIgnoreCase(flagString))
                return flag;
        }
        return null;
    }


    private static transient Material[] byId;

    protected void give(Player player, ItemStack item) {
        if (hasInventoryFull(player))
            player.getWorld().dropItem(player.getLocation(), item);
        else
            player.getInventory().addItem(item);
    }

    static {
        if (!NMS.isNewVersion()) {
            byId = new Material[0];
            for (Material material : Material.values()) {
                if (byId.length <= material.getId()) {
                    byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
                }
                byId[material.getId()] = material;
            }
        }
    }

    /**
     * Remove a certain number of items from a player's inventory
     *
     * @param player
     *            - Player who will have items removed
     * @param amount
     *            - Number of items to remove
     * @param itemStack
     *            - ItemStack to be removed
     */
    protected static void removeItems(Player player, int amount, ItemStack itemStack) {
        int slot = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is.isSimilar(itemStack) && amount > 0) {
                int currentAmount = is.getAmount() - amount;
                amount -= is.getAmount();
                if (currentAmount <= 0) {
                    if (slot == 40) {
                        player.getInventory().setItemInOffHand(null);
                    } else {
                        player.getInventory().removeItem(is);
                    }
                } else {
                    is.setAmount(currentAmount);
                }
            }
            slot++;
        }
        player.updateInventory();
    }

    protected <T> List<T> reverse(List<T> list) {
        List<T> tmpList = new ArrayList<>();
        for (int index = list.size() - 1; index != -1; index--)
            tmpList.add(list.get(index));
        return tmpList;
    }

    protected BlockFace getClosestFace(float direction) {

        direction = direction % 360;

        if (direction < 0)
            direction += 360;

        direction = Math.round(direction / 45);

        switch ((int) direction) {
            case 1:
                return BlockFace.NORTH_WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH;
            case 7:
                return BlockFace.SOUTH_WEST;
            default:
                return BlockFace.WEST;
        }
    }

    protected Object getPrivateField(Object object, String field)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = field.equals("commandMap") ? clazz.getDeclaredField(field)
                : field.equals("knownCommands") ? NMS.isNewVersion()
                ? clazz.getSuperclass().getDeclaredField(field) : clazz.getDeclaredField(field) : null;
        assert objectField != null;
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    /**
     * Allows to make an itemstack shine
     *
     */
    public void glow(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        if (NMS.getNMSVersion() != 1.7)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Allows you to clear a player's inventory, remove potion effects and put
     * him on life support
     */
    protected void clearPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setBoots(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setHelmet(null);
        player.getPlayer().setItemOnCursor(null);
        player.getPlayer().setFireTicks(0);
        player.getPlayer().getOpenInventory().getTopInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.getPlayer().getActivePotionEffects().forEach(e -> player.getPlayer().removePotionEffect(e.getType()));
    }
}
