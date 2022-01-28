package br.com.eskaryos.rankup.utils.api;

import org.bukkit.enchantments.Enchantment;

public enum EnchantAPI {
    PROTECTION("PROTECTION_ENVIRONMENTAL", "PROTECTION_ENVIRONMENTAL"),
    UNBREAKING("DURABILITY", "DURABILITY"),
    FIREPROTECTION("PROTECTION_FIRE", "PROTECTION_FIRE"),
    FEATHERFALLING("PROTECTION_FALL", "PROTECTION_FALL"),
    BLASTPROTECTION("PROTECTION_EXPLOSIONS", "PROTECTION_EXPLOSIONS"),
    SHARPNESS("DAMAGE_ALL", "DAMAGE_ALL"),
    KNOCKBACK("KNOCKBACK", "KNOCKBACK"),
    FIREASPECT("FIRE_ASPECT", "FIRE_ASPECT"),
    LOOTING("LOOT_BONUS_MOBS", "LOOT_BONUS_MOBS"),
    FORTUNE("LOOT_BONUS_BLOCKS", "LOOT_BONUS_BLOCKS"),
    SILKTOUCH("SILK_TOUCH", "SILK_TOUCH"),
    EFFICIENCY("DIG_SPEED", "DIG_SPEED"),
    PUNCH("ARROW_KNOCKBACK", "ARROW_KNOCKBACK"),
    POWER("ARROW_DAMAGE", "ARROW_DAMAGE");


    private final String pre19Enchant;
    private final String post19Enchant;

    EnchantAPI(String pre19Enchant, String post19Enchant) {
        this.pre19Enchant = pre19Enchant;
        this.post19Enchant = post19Enchant;
    }

    public Enchantment getEnchant() {
        try {
            return Enchantment.getByName(this.pre19Enchant);
        } catch (IllegalArgumentException e) {
            try {
                return Enchantment.getByName(this.post19Enchant);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }
}
