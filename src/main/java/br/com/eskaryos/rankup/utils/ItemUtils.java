package br.com.eskaryos.rankup.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
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
