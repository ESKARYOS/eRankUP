package br.com.eskaryos.rankup.upgrade;

import br.com.eskaryos.rankup.requirements.RequirementType;
import org.bukkit.inventory.ItemStack;

public class Upgrade {

    private RequirementType type;
    private int value;
    private ItemStack icon;

    public Upgrade(RequirementType type, int value, ItemStack icon){
        this.type = type;
        this.value = value;
        this.icon = icon;
    }

    public RequirementType getType() {
        return type;
    }

    public void setType(RequirementType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }
}
