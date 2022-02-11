package br.com.eskaryos.rankup.cmds;

import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            Profile profile = DataMain.getProfile(p.getUniqueId());
            if (profile.getNext()==null)return true;
            return profile.getRank().getOrder() >= profile.getNext().getOrder();
        }
        return false;
    }
}
