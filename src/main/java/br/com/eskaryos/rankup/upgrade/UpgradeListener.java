package br.com.eskaryos.rankup.upgrade;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.listener.Listeners;
import br.com.eskaryos.rankup.requirements.RequirementType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradeListener extends Listeners {


    @EventHandler
    public void event(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(p.getItemInHand()==null)return;
        if(!p.getItemInHand().hasItemMeta())return;
        if(UpgradeManager.hasUpgrade(p.getItemInHand())){
            Upgrade up = UpgradeManager.getUpgrade(p.getItemInHand());
            UpgradeManager.useUpgrade(p,up, DataMain.getProfile(p.getUniqueId()).getNext().getRequirementsbytype()
                    .get(RequirementType.MINE).get(0));
        }
    }


}
