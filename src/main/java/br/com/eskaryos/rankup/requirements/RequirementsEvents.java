package br.com.eskaryos.rankup.requirements;

import br.com.eskaryos.rankup.listener.Listeners;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class RequirementsEvents extends Listeners {

    @EventHandler
    public void Craft(CraftItemEvent e) {
        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            RequirementMain.setCraftRequirement(p,e);
        }
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
    public void Pickup(PlayerPickupItemEvent e){
        RequirementMain.setPickupRequirement(e.getPlayer(),e);
    }
    @EventHandler
    public void Kill(EntityDeathEvent e){
        RequirementMain.setKillRequirement(e);
    }
}
