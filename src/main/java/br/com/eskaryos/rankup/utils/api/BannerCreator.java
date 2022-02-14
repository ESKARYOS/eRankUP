package br.com.eskaryos.rankup.utils.api;

import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
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
        this.baseColor = baseColor;
        banner = getBannerItemStack();
        meta = (BannerMeta) banner.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.patterns = patternList;

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

    public ItemStack getBannerItemStack(){
        if(JavaUtils.getVersion().contains("1_18")){
            Material material = Material.valueOf(baseColor.name()+"_BANNER");
            return new ItemStack(material);
        }
        return new ItemStack(Objects.requireNonNull(Material.matchMaterial("425")),1);
    }
}
