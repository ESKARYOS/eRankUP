package br.com.eskaryos.rankup.requirements;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Requirement {

    private ItemStack item;
    private String entity;
    private int value = 0;
    private int max;

    public Requirement (ItemStack item,String entity,int max){
        this.item = item;
        this.entity = entity;
        this.max = max;
    }

}
