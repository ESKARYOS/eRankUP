package br.com.eskaryos.rankup.listener.events;

import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.requirements.RequirementMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemMendEvent;

public class RequirementsEvents extends Listeners {

    @EventHandler
    public void Craft(CraftItemEvent e) {
        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            RequirementMain.setCraftRequirement(p,e);
        }
    }

    @EventHandler
    public void AnvilEvent(InventoryClickEvent e){
        RequirementMain.setEnchantAnvilRequirement(e);
    }

    @EventHandler
    public void event(EnchantItemEvent e){
        RequirementMain.setEnchantRequirement(e);
    }

    @EventHandler
    public void Repair(PlayerItemMendEvent e){
        e.getPlayer().sendMessage(e.getItem().getType().name());
    }

    @EventHandler
    public void Consume(PlayerItemConsumeEvent e){
        RequirementMain.setConsumeRequirement(e);
    }

    @EventHandler
    public void Furnace(InventoryClickEvent e){
        RequirementMain.setCookRequirement(e);
    }

    @EventHandler
    public void Mine(BlockBreakEvent e){
        RequirementMain.setMineRequirement(e.getPlayer(),e);
    }

    @EventHandler
    public void Place(BlockPlaceEvent e){
        RequirementMain.setPlaceRequirement(e.getPlayer(),e);
    }

    @EventHandler
    public void Kill(EntityDeathEvent e){
        RequirementMain.setKillRequirement(e);
    }

    @EventHandler
    public void event(PlayerFishEvent e){
        RequirementMain.setFishRequirement(e.getPlayer(),e);
    }
}
