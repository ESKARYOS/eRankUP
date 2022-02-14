package br.com.eskaryos.rankup.requirements;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
import java.util.stream.Collectors;

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
        ItemStack block = new ItemStack(e.getBlock().getType(),1,e.getBlock().getData());
        Rank next = profile.getNext();
        if(next.getRequirements().get(RequirementType.MINE).isEmpty())return;
        updateRequirement(p,RequirementType.MINE,block,1);
        e.getBlock().setMetadata("mine",new FixedMetadataValue(Main.plugin,true));
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
                updateRequirement(p,RequirementType.FISH,item,item.getAmount());
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
        ItemStack block = new ItemStack(e.getBlock().getType(),1,e.getBlock().getData());
        if(profile.getNext().getRequirements().get(RequirementType.PLACE).isEmpty())return;
        updateRequirement(p,RequirementType.PLACE,block,1);
    }

    public static void setKillRequirement(EntityDeathEvent e) {
        if(e.getEntity().getKiller()!=null){
            Player p = e.getEntity().getKiller();
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if(profile.getNext()==null)return;
            if(profile.getNext().getRequirements().get(RequirementType.KILL).isEmpty())return;
            updateRequirement(p,RequirementType.KILL,e.getEntity(),1);
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
        updateRequirement(p,RequirementType.CRAFT,e.getRecipe().getResult(),realAmount);
    }

    /***
     * Method to update Requirement
     * @param p Player to update
     * @param type Type of Requirement
     * @param obj Object to requirement (item/mob)
     * @param value Value to update
     */
    public static void updateRequirement(Player p,RequirementType type,Object obj, int value){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        Rank next =profile.getNext();

        if(next.getRequirements().get(type).isEmpty())return;
        if(type!=RequirementType.KILL){
            ItemStack item = (ItemStack) obj;
            List<Requirement> requirement = next.getRequirements().get(type).
                    stream().filter(t -> t.getItem().isSimilar(item)).collect(Collectors.toList());
            if(requirement.isEmpty()) return;
            if(requirement.get(0).getValue()>=requirement.get(0).getMax())return;
            if((requirement.get(0).getValue()+1)>requirement.get(0).getMax())return;
            requirement.get(0).setValue(requirement.get(0).getValue()+value);
            sendCompletedMessage(p,requirement.get(0));
            return;
        }
        Entity e = (Entity) obj;
        System.out.println(e.getType().name());
        List<Requirement> requirement = next.getRequirements().get(type).
                stream().filter(t -> e.getType().name().equals(t.getEntity().toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        if(requirement.isEmpty())return;
        if(requirement.get(0).getValue()>=requirement.get(0).getMax())return;
        if((requirement.get(0).getValue()+1)>requirement.get(0).getMax())return;
        requirement.get(0).setValue(requirement.get(0).getValue()+value);
        sendCompletedMessage(p,requirement.get(0));
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
