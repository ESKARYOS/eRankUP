package br.com.eskaryos.rankup.requirements;

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
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequirementMain {

    /***
     * Method to get requirement value
     * @param p Player to get
     * @param name Requirement name
     * @return return requirement value
     */
    public static String getRequirementValue(Player p,String name){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return "§cERROR";
        Rank next = DataMain.getProfile(p.getUniqueId()).getNext();
       try{
           return next.getRequirements().get(name).getValue()+"";
       }catch (Exception e){
           return "§cERROR";
       }
    }
    /***
     * Method to get requirement max value
     * @param p Player to get
     * @param name Requirement name
     * @return return requirement value
     */
    public static String getRequirementMaxValue(Player p,String name){
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return "§cERROR";
        Rank next = DataMain.getProfile(p.getUniqueId()).getNext();
        try{
            return next.getRequirements().get(name).getMax()+"";
        }catch (Exception e){
            return "§cERROR";
        }
    }

    /***
     * Method to update Mine requirement
     * @param p Player to update
     */


    public static void setMineRequirement(Player p, BlockBreakEvent e) {
        Profile profile = DataMain.getProfile(p.getUniqueId());
        RequirementType type = RequirementType.MINE;
        if(profile.getNext()==null)return;
        Rank next = profile.getNext();

        List<Requirement> list = next.getRequirementsbytype().get(type);
        if(list.isEmpty())return;
        ItemStack item = new ItemStack(e.getBlock().getType(),e.getBlock().getData());

        if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(type,list,item));}

        if(next.getLastRequirement()==null)return;

        /**If the last requirement is not the same type, it returns*/
        if(next.getLastRequirement().getType()!=type){next.setLastRequirement(getLastRequirement(type,list,item));}
        Requirement requirement = next.getLastRequirement();
        /**Update values if requirement matchess*/
        if(requirement.getValue()>=requirement.getMax())return;
        if((requirement.getValue()+1)>requirement.getMax())return;
        requirement.setValue(requirement.getValue()+1);
        sendCompletedMessage(p,requirement);

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
                Rank next = profile.getNext();
                List<Requirement> requirements = next.getRequirementsbytype().get(RequirementType.FISH);
                if(requirements.isEmpty())return;

                if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(RequirementType.FISH,requirements,item));}
                if(next.getLastRequirement()==null)return;
                if(next.getLastRequirement().getType()!=RequirementType.FISH){next.setLastRequirement(getLastRequirement(RequirementType.FISH,requirements,item));}

                if(next.getLastRequirement()==null)return;
                Requirement requirement = next.getLastRequirement();
                if(requirement.getValue()>=requirement.getMax())return;
                if((requirement.getValue()+1)>requirement.getMax())return;
                requirement.setValue(requirement.getValue()+1);
                sendCompletedMessage(p,requirement);
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
        RequirementType type = RequirementType.PLACE;
        if(profile.getNext()==null)return;
        Rank next = profile.getNext();

        List<Requirement> list = next.getRequirementsbytype().get(type);
        if(list.isEmpty())return;
        ItemStack item = new ItemStack(e.getBlock().getType(),e.getBlock().getData());

        if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(type,list,item));}

        if(next.getLastRequirement()==null)return;

        /**If the last requirement is not the same type, it returns*/
        if(next.getLastRequirement().getType()!=type){next.setLastRequirement(getLastRequirement(type,list,item));}
        if(next.getLastRequirement()==null)return;

        Requirement requirement = next.getLastRequirement();
        /**Update values if requirement matchess*/
        if(requirement.getValue()>=requirement.getMax())return;
        if((requirement.getValue()+1)>requirement.getMax())return;
        requirement.setValue(requirement.getValue()+1);
        sendCompletedMessage(p,requirement);
    }

    public static void setKillRequirement(EntityDeathEvent e) {
        if(e.getEntity().getKiller()!=null){
            if(e.getEntity().getKiller() != null){
                Player p = e.getEntity().getKiller();
                Profile profile = DataMain.getProfile(p.getUniqueId());
                RequirementType type = RequirementType.KILL;
                if(profile.getNext()==null)return;
                Rank next = profile.getNext();

                List<Requirement> list = next.getRequirementsbytype().get(type);
                if(list.isEmpty())return;
                Entity item = e.getEntity();

                if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(type,list,item));}
                if(next.getLastRequirement()==null)return;

                /**If the last requirement is not the same type, it returns*/
                if(next.getLastRequirement().getType()!=type){next.setLastRequirement(getLastRequirement(type,list,item));}
                if(next.getLastRequirement()==null)return;

                Requirement requirement = next.getLastRequirement();
                /**Update values if requirement matchess*/
                if(requirement.getValue()>=requirement.getMax())return;
                if((requirement.getValue()+1)>requirement.getMax())return;
                requirement.setValue(requirement.getValue()+1);
                sendCompletedMessage(p,requirement);
            }
        }

    }
    public static void setCookRequirement(InventoryClickEvent e){
        if(e.getView().getType() != InventoryType.FURNACE)return;
        if(!(e.getWhoClicked() instanceof Player))return;
        Player p = (Player) e.getWhoClicked();
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;
        Rank next = profile.getNext();
        RequirementType type = RequirementType.COOK;
        List<Requirement> list = next.getRequirementsbytype().get(type);
        if(list.isEmpty())return;

        if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(type,list,e.getCurrentItem()));}
        if(next.getLastRequirement()==null)return;

        if(next.getLastRequirement().getType()!=type){next.setLastRequirement(getLastRequirement(type,list,e.getCurrentItem()));}
        if(next.getLastRequirement()==null)return;
        Requirement requirement = next.getLastRequirement();
        if(requirement.getValue()>=requirement.getMax())return;
        if((requirement.getValue()+1)>requirement.getMax())return;
        requirement.setValue(requirement.getValue()+ Objects.requireNonNull(e.getCurrentItem()).getAmount());
        sendCompletedMessage(p,requirement);
    }



    public static void setConsumeRequirement(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;
        Rank next = profile.getNext();
        RequirementType type = RequirementType.CONSUME;
        List<Requirement> list = next.getRequirementsbytype().get(type);
        if(list.isEmpty())return;

        if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(type,list,e.getItem()));}
        if(next.getLastRequirement()==null)return;

        if(next.getLastRequirement().getType()!=type){next.setLastRequirement(getLastRequirement(type,list,e.getItem()));}
        if(next.getLastRequirement()==null)return;
        Requirement requirement = next.getLastRequirement();
        if(requirement.getValue()>=requirement.getMax())return;
        if((requirement.getValue()+1)>requirement.getMax())return;
        requirement.setValue(requirement.getValue()+1);
        sendCompletedMessage(p,requirement);
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

        Rank next = profile.getNext();
        List<Requirement> requirements = next.getRequirementsbytype().get(RequirementType.CRAFT);
        if(requirements.isEmpty())return;
        if(clickType.isShiftClick()) {
            int lowerAmount = craftedItem.getMaxStackSize() + 1000;
            for(ItemStack actualItem : Inventory.getContents()) {
                if(!actualItem.getType().equals(Material.AIR) && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) {
                    lowerAmount = actualItem.getAmount();
                }
            }
            realAmount = lowerAmount * craftedItem.getAmount();
        }
        if(requirements.isEmpty())return;

        if(next.getLastRequirement()==null){next.setLastRequirement(getLastRequirement(RequirementType.CRAFT,requirements,craftedItem));}
        if(next.getLastRequirement()==null)return;

        if(next.getLastRequirement().getType()!=RequirementType.CRAFT){next.setLastRequirement(getLastRequirement(RequirementType.CRAFT,requirements,craftedItem));}
        if(next.getLastRequirement()==null)return;
        Requirement requirement = next.getLastRequirement();
        if(requirement.getValue()>=requirement.getMax())return;
        if((requirement.getValue()+1)>requirement.getMax())return;
        requirement.setValue(requirement.getValue()+realAmount);
        sendCompletedMessage(p,requirement);

    }

    public static void setEnchantAnvilRequirement(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if(profile.getNext()==null)return;
            Rank next = profile.getNext();
            List<Requirement> requirements = next.getRequirementsbytype().get(RequirementType.ENCHANT);
            if(requirements.isEmpty())return;

            if(e.getInventory() instanceof AnvilInventory){
                AnvilInventory anvil = (AnvilInventory)e.getInventory();
                InventoryView view = e.getView();
                int rawSlot = e.getRawSlot();
                if(rawSlot == view.convertSlot(rawSlot)){
                    if(rawSlot == 2){
                        ItemStack[] items = anvil.getContents();
                        ItemStack item1 = items[0];
                        ItemStack item2 = items[1];
                        EnchantmentStorageMeta meta = null;
                        if(e.getCurrentItem()==null)return;
                        if (item1.getType().equals(Material.ENCHANTED_BOOK)){meta = (EnchantmentStorageMeta) item1.getItemMeta();}
                        if (item2.getType().equals(Material.ENCHANTED_BOOK)){meta = (EnchantmentStorageMeta) item2.getItemMeta();}

                        assert meta!=null;
                        EnchantmentStorageMeta finalMeta = meta;
                        List<Requirement> requirementList = requirements.stream().filter(t->
                                finalMeta.getStoredEnchants().containsKey(t.getEnchantment())).collect(Collectors.toList());
                        if(requirementList.isEmpty()) return;
                        if(requirementList.get(0).getValue()>=requirementList.get(0).getMax())return;
                        if((requirementList.get(0).getValue()+1)>requirementList.get(0).getMax())return;
                        requirementList.get(0).setValue(requirementList.get(0).getValue()+1);
                        sendCompletedMessage(p,requirementList.get(0));
                    }
                }
            }
        }
    }

    public static void setEnchantRequirement(EnchantItemEvent e){
        Player p = e.getEnchanter();
        Profile profile = DataMain.getProfile(p.getUniqueId());
        if(profile.getNext()==null)return;
        Rank next = profile.getNext();
        List<Requirement> requirements = next.getRequirementsbytype().get(RequirementType.ENCHANT);
        if(requirements.isEmpty())return;
        ItemStack item = e.getItem();
        List<Requirement> requirement = requirements.stream().filter(t->t.getItem().isSimilar(item)).collect(Collectors.toList());

        List<Requirement> requirementList = requirement.stream().filter(t->e.getEnchantsToAdd().containsKey(t.getEnchantment())).collect(Collectors.toList());

        if(requirementList.isEmpty()) return;
        if(requirementList.get(0).getValue()>=requirementList.get(0).getMax())return;
        if((requirementList.get(0).getValue()+1)>requirementList.get(0).getMax())return;
        requirementList.get(0).setValue(requirementList.get(0).getValue()+1);
        sendCompletedMessage(p,requirementList.get(0));
    }

    /***
     * Method to update Requirement
     * @param type Type of Requirement
     * @param obj Object to requirement (item/mob)
     */
    public static Requirement getLastRequirement(RequirementType type,List<Requirement> requirements,Object obj){
        if(type!=RequirementType.KILL){

            ItemStack item = (ItemStack) obj;
            List<Requirement> requirement = requirements.
                    stream().filter(t -> t.getItem().isSimilar(item)).collect(Collectors.toList());
            if(requirement.isEmpty()) return null;
            return requirement.get(0);
        }
        Entity e = (Entity) obj;
        List<Requirement> requirement = requirements.stream().filter(t -> e.getType().name().equals(t.getEntity().toUpperCase(Locale.ROOT))).collect(Collectors.toList());
        if(requirement.isEmpty()) return null;
        return requirement.get(0);
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
