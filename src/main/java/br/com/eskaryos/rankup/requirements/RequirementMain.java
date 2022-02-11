package br.com.eskaryos.rankup.requirements;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.utils.placeholder.RankHolder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Locale;

public class RequirementMain {

    /***
     * Method to get requirement value
     * @param p Player to get
     * @param type Requirement type
     * @param requirement Requirement List
     * @return return requirement value
     */
    public static String getRequirementValue(Player p,RequirementType type, int requirement){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return "§cERROR";
        Rank next = DataMain.getProfile(p.getUniqueId()).getNext();
        final List<Requirement> requirements = next.getRequirements().get(type);
       try{
           return requirements.get(requirement).getValue()+"";
       }catch (Exception e){
           return "§cERROR";
       }
    }
    /***
     * Method to get requirement max value
     * @param p Player to get
     * @param type Requirement type
     * @param requirement Requirement List
     * @return return requirement value
     */
    public static String getRequirementMaxValue(Player p,RequirementType type, int requirement){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return "§cERROR";
        Rank next = DataMain.getProfile(p.getUniqueId()).getNext();
        final List<Requirement> requirements = next.getRequirements().get(type);
        try{
            return requirements.get(requirement).getMax()+"";
        }catch (Exception e){
            return "§cERROR";
        }
    }

    /***
     * Method to update Mine requirement
     * @param p Player to update
     * @param e Event to update
     */
    public static void setMineRequirement(Player p, BlockBreakEvent e) {
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;
        Block block = e.getBlock();
        Material type = block.getType();
        byte data = block.getData();
        if(profile.getNext().getRequirements().get(RequirementType.MINE).isEmpty())return;
        for(Requirement requirement : profile.getNext().getRequirements().get(RequirementType.MINE)){
            if(requirement.getItem().getType().equals(type) && requirement.getItem().getDurability() ==
            data){
                if(requirement.getValue()>=requirement.getMax())return;
                if((requirement.getValue()+1)>requirement.getMax())return;
                requirement.setValue(requirement.getValue()+1);
                sendCompletedMessage(p,requirement);
                e.getBlock().setMetadata("mine",new FixedMetadataValue(Main.plugin,true));
                return;
            }
        }

    }

    /**
     * Method to update Fish requirement
     * @param p Player to update
     * @param e Event to update
     */
    public static void setFishRequirement(Player p, PlayerFishEvent e){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (profile.getNext() == null) return;
            if (e.getCaught() instanceof Item) {
                ItemStack item = ((Item) e.getCaught()).getItemStack();
                if (profile.getNext().getRequirements().get(RequirementType.FISH).isEmpty()) return;
                for (Requirement requirement : profile.getNext().getRequirements().get(RequirementType.FISH)) {
                    if (item.isSimilar(requirement.getItem())) {
                        if (requirement.getValue() >= requirement.getMax()) return;
                        if ((requirement.getValue() + 1) > requirement.getMax()) return;
                        requirement.setValue(requirement.getValue() + 1);
                        sendCompletedMessage(p,requirement);
                        return;
                    }
                }
            }
        }
    }

    /***
     * Method to update Place requirement
     * @param p Player to update
     * @param e Event to update
     */

    public static void setPlaceRequirement(Player p, BlockPlaceEvent e) {
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;

        Block block = e.getBlock();
        Material type = block.getType();
        byte data = block.getData();
        if(profile.getNext().getRequirements().get(RequirementType.PLACE).isEmpty())return;

        for(Requirement requirement : profile.getNext().getRequirements().get(RequirementType.PLACE)){
            if(requirement.getItem().getType().equals(type) && requirement.getItem().getDurability() ==
                    data){
                if(requirement.getValue()>=requirement.getMax())return;
                if((requirement.getValue()+1)>requirement.getMax())return;
                requirement.setValue(requirement.getValue()+1);
                sendCompletedMessage(p,requirement);
                return;
            }
        }
    }

    public static void setKillRequirement(EntityDeathEvent e) {
        if(e.getEntity().getKiller()!=null){
            Player p = e.getEntity().getKiller();
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if(profile.getNext()==null)return;
            if(profile.getNext().getRequirements().get(RequirementType.KILL).isEmpty())return;


            for(Requirement requirement : profile.getNext().getRequirements().get(RequirementType.KILL)){
                if(e.getEntity().getType().name().equals(requirement.getEntity().toUpperCase(Locale.ROOT))){
                    if(requirement.getValue()>=requirement.getMax())return;
                    if((requirement.getValue()+1)>requirement.getMax())return;
                    requirement.setValue(requirement.getValue()+1);
                    sendCompletedMessage(p,requirement);
                    return;
                }
            }
        }

    }

    /**
     * Method to update Requirement craft
     * @param p Player to update
     * @param e Event to update
     */
    public static void setCraftRequirement(Player p, CraftItemEvent e) {
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;

        ItemStack craftedItem = e.getInventory().getResult();
        Inventory Inventory = e.getInventory();
        ClickType clickType = e.getClick();
        assert craftedItem != null;
        int realAmount = craftedItem.getAmount();
        if(profile.getNext().getRequirements().get(RequirementType.CRAFT).isEmpty())return;

        if(clickType.isShiftClick()) {
            int lowerAmount = craftedItem.getMaxStackSize() + 1000;
            for(ItemStack actualItem : Inventory.getContents()) {
                if(!actualItem.getType().equals(Material.AIR) && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) {
                    lowerAmount = actualItem.getAmount();
                }
            }
            realAmount = lowerAmount * craftedItem.getAmount();
        }

        RequirementType type = RequirementType.CRAFT;
        Rank rank = profile.getNext();

        final List<Requirement> requirements = rank.getRequirements().get(type);
        if(requirements.isEmpty())return;
        setValueisTrue(p,requirements,e.getRecipe().getResult(),realAmount);
    }
    private static void setValueisTrue(Player p, List<Requirement> list, ItemStack item, int realAmount){
        for(Requirement requirement : list){
            if(requirement.getItem().isSimilar(item)){
                if(requirement.getValue()>=requirement.getMax())return;
                int var = Math.min((requirement.getValue() + realAmount), requirement.getMax());
                requirement.setValue(var);
                sendCompletedMessage(p,requirement);
                return;
            }
        }
    }
    public static void sendCompletedMessage(Player p,Requirement requirement){
        if(requirement.getValue()>=requirement.getMax()){
            requirement.sendBar(p);
            requirement.sendTitle(p);
            requirement.sendSound(p);
            if(!requirement.getCompletedMessage().isEmpty()){
                for(String k : requirement.getCompletedMessage()){
                    if(!k.isEmpty()){
                        p.sendMessage(RankHolder.hook(p,k));
                    }
                }
            }
        }
    }
}
