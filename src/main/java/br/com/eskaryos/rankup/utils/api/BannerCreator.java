package br.com.eskaryos.rankup.utils.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class BannerCreator {

    private ItemStack banner;
    private BannerMeta meta;
    private DyeColor baseColor;
    private List<Pattern> patterns;

    public BannerCreator(DyeColor baseColor,List<Pattern> patternList){
        banner = new ItemStack(Objects.requireNonNull(Material.matchMaterial("425")),1);
        meta = (BannerMeta) banner.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.patterns = patternList;
        this.baseColor = baseColor;
        make();
    }

    private void make(){
        meta.setBaseColor(baseColor);
        meta.setPatterns(patterns);

        banner.setItemMeta(meta);
    }

    public ItemStack getBanner(){
        return banner.clone();
    }
}
